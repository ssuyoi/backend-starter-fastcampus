package com.backendstarter.crash.config;

import com.backendstarter.crash.model.sessionspeaker.SessionSpeaker;
import com.backendstarter.crash.model.sessionspeaker.SessionSpeakerPostRequestBody;
import com.backendstarter.crash.model.user.UserSignUpRequestBody;
import com.backendstarter.crash.service.SessionSpeakerService;
import com.backendstarter.crash.service.UserService;
import java.util.stream.IntStream;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    private static final Faker faker = new Faker();

    @Autowired
    private UserService userService;

    @Autowired
    private SessionSpeakerService sessionSpeakerService;

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                // 유저 및 세션스피커 생성
                createTestUsers();
                createTestSessionSpeakers(10);
            }
        };
    }

    private void createTestUsers() {
        userService.signUp(
            new UserSignUpRequestBody("jayce", "1234", "Dev Jayce", "jayce@crash.com"));
        userService.signUp(new UserSignUpRequestBody("jay", "1234", "Dev Jay", "jay@crash.com"));
        userService.signUp(new UserSignUpRequestBody("rose", "1234", "Dev Rose", "rose@crash.com"));
        userService.signUp(new UserSignUpRequestBody("rosa", "1234", "Dev Rosa", "rosa@crash.com"));
    }

    private void createTestSessionSpeakers(int numberOfSpeakers) {
        var sessionSpeakers =
            IntStream.range(0, numberOfSpeakers)
                .mapToObj(i -> createTestSessionSpeakers()).toList();
    }

    private SessionSpeaker createTestSessionSpeakers() {
        var name = faker.name().fullName();
        var company = faker.company().name();
        // 회사 소개 부분을 셰익스피어의 로미오와 줄리엣 구절로 랜덤 생성
        var description = faker.shakespeare().romeoAndJulietQuote();

        return sessionSpeakerService.createSessionSpeaker(
            new SessionSpeakerPostRequestBody(company, name, description)
        );
    }
}
