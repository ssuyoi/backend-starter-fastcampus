package com.backendstarter.crash.config;

import com.backendstarter.crash.model.coinbase.PriceResponse;
import com.backendstarter.crash.model.crashsession.CrashSessionCategory;
import com.backendstarter.crash.model.crashsession.CrashSessionPostRequestBody;
import com.backendstarter.crash.model.exchange.ExchangeResponse;
import com.backendstarter.crash.model.sessionspeaker.SessionSpeaker;
import com.backendstarter.crash.model.sessionspeaker.SessionSpeakerPostRequestBody;
import com.backendstarter.crash.model.user.UserSignUpRequestBody;
import com.backendstarter.crash.service.CrashSessionService;
import com.backendstarter.crash.service.SessionSpeakerService;
import com.backendstarter.crash.service.UserService;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

@Configuration
public class ApplicationConfiguration {

    private static final RestClient restClient = RestClient.create();
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfiguration.class);

    private static final Faker faker = new Faker();

    @Value("${korea-exim.api-key}")
    private String koreaEximApiKey;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionSpeakerService sessionSpeakerService;

    @Autowired
    private CrashSessionService crashSessionService;

    /**
     * 애플리케이션 시작 시 테스트 데이터를 초기화하는 {@link ApplicationRunner} 빈을 등록
     * <p>
     * 테스트 유저 4명과 10명의 세션 발표자(및 각 발표자에 연결된 세션들)를 자동 생성
     * </p>
     *
     * @return 애플리케이션 구동 시 실행될 {@link ApplicationRunner} 인스턴스
     */
    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                createTestUsers();
                createTestSessionSpeakers(10);
                // Bitcoin USD 가격 조회
                var bitcoinUsdPrice = getBitcoinUsdPrice();
                // USD to KRW 환율 조회
                var usdToKrwExchangeRate = getUsdToKrwExchangeRate();
                // Bitcoin KRW 가격 계산
                var koreanPremium = 1.1;
                var bitcoinKrwPrice = bitcoinUsdPrice * usdToKrwExchangeRate * koreanPremium;

                logger.info(String.format("BTC KRW: %.2f", bitcoinKrwPrice));
            }
        };
    }

    /**
     * Bitcoin USD 가격 조회
     */
    private Double getBitcoinUsdPrice() {
        var response = restClient
            .get()
            .uri("https://api.coinbase.com/v2/prices/BTC-USD/buy")
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                // TODO: 클라이언트 에러 예외 처리
                logger.error(new String(res.getBody().readAllBytes(), StandardCharsets.UTF_8));
            })
            .body(PriceResponse.class);

        assert response != null;
        return Double.parseDouble(response.data().amount());
    }

    private Double getUsdToKrwExchangeRate() {
        var response = restClient
            .get()
            .uri(
                "https://oapi.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey="+ koreaEximApiKey +"&searchdate=20180102&data=AP01")
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                logger.error(new String(res.getBody().readAllBytes(), StandardCharsets.UTF_8));
            })
            .body(ExchangeResponse[].class);

        assert response != null;

        var usdToKrwExchangeRate =
            Arrays.stream(response)
                .filter(exchangeResponse -> exchangeResponse.cur_unit().equals("USD"))
                .findFirst()
                .orElseThrow();

        return Double.parseDouble(usdToKrwExchangeRate.deal_bas_r().replace(",", ""));
    }

    /**
     * 테스트용 유저 계정 4개 생성
     */
    private void createTestUsers() {
        userService.signUp(
            new UserSignUpRequestBody("jayce", "1234", "Dev Jayce", "jayce@crash.com"));
        userService.signUp(new UserSignUpRequestBody("jay", "1234", "Dev Jay", "jay@crash.com"));
        userService.signUp(new UserSignUpRequestBody("rose", "1234", "Dev Rose", "rose@crash.com"));
        userService.signUp(new UserSignUpRequestBody("rosa", "1234", "Dev Rosa", "rosa@crash.com"));
    }

    /**
     * 지정한 수만큼 테스트용 세션 발표자를 생성하고, 각 발표자에게 1~4개의 세션을 무작위로 배정
     *
     * @param numberOfSpeakers 생성할 세션 발표자 수
     */
    private void createTestSessionSpeakers(int numberOfSpeakers) {
        var sessionSpeakers =
            IntStream.range(0, numberOfSpeakers)
                .mapToObj(i -> createTestSessionSpeakers()).toList();

        sessionSpeakers.forEach(
            sessionSpeaker -> {
                int numberOfSessions = new Random().nextInt(4) + 1;
                IntStream.range(0, numberOfSessions)
                    .forEach(i -> createTestCrashSessions(sessionSpeaker));
            }
        );
    }

    /**
     * Faker 라이브러리를 사용해 랜덤한 이름·소속 회사·소개를 가진 테스트용 세션 발표자 1명을 생성
     * <p>
     * 발표자 소개(description)는 셰익스피어의 《로미오와 줄리엣》 구절로 채워짐
     * </p>
     *
     * @return 생성된 {@link SessionSpeaker} 도메인 객체
     */
    private SessionSpeaker createTestSessionSpeakers() {
        var name = faker.name().fullName();
        var company = faker.company().name();
        var description = faker.shakespeare().romeoAndJulietQuote();

        return sessionSpeakerService.createSessionSpeaker(
            new SessionSpeakerPostRequestBody(company, name, description)
        );
    }

    /**
     * Faker 라이브러리를 사용해 랜덤한 제목·본문·카테고리·일시를 가진 테스트용 크래시 세션 1개 생성
     * <p>
     * 세션 일시는 오늘 기준 내일 또는 모레로 무작위 설정됩니다.
     * </p>
     *
     * @param sessionSpeaker 세션에 연결할 발표자 정보
     */
    private void createTestCrashSessions(SessionSpeaker sessionSpeaker) {
        var title = faker.book().title();
        var body = faker.shakespeare().asYouLikeItQuote()
            + faker.shakespeare().hamletQuote()
            + faker.shakespeare().kingRichardIIIQuote();

        crashSessionService.createCrashSession(new CrashSessionPostRequestBody(
            title,
            body,
            getRandomCategory(),
            ZonedDateTime.now().plusDays(new Random().nextInt(2) + 1),
            sessionSpeaker.speakerId()
        ));
    }

    /**
     * {@link CrashSessionCategory} 열거형 값 중 하나를 무작위로 반환합니다.
     *
     * @return 무작위로 선택된 {@link CrashSessionCategory}
     */
    private CrashSessionCategory getRandomCategory() {
        var categories = CrashSessionCategory.values();
        int randomIndex = new Random().nextInt(categories.length);
        return categories[randomIndex];
    }
}
