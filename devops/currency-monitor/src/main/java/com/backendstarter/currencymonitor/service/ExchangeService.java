package com.backendstarter.currencymonitor.service;

import com.backendstarter.currencymonitor.model.exchange.ExchangeResponse;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

/**
 * 한국수출입은행 환율 API 연동 서비스
 */
@Service
public class ExchangeService {

    private static final RestClient restClient = RestClient.create();
    private final String apiUri;
    private final String authKey;

    public ExchangeService(
        @Value("${kexim.api-uri}") String apiUri,
        @Value("${kexim.auth-key}") String authKey) {
        this.apiUri = apiUri;
        this.authKey = authKey;
    }

    /**
     * 특정 통화의 환율 반환
     *  - API 응답이 없으면 404
     *  - 해당 통화가 없으면 USD 기본값 반환.
     *
     * @param currency 통화 코드 (예: USD, JPY)
     */
    public ExchangeResponse getExchangeByCurrency(String currency) {
        var exchangesResponse =
            restClient
                .get()
                .uri(apiUri + authKey)
                .retrieve()
                .body(ExchangeResponse[].class);

        if (exchangesResponse == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return Arrays.stream(exchangesResponse)
            .filter(exchangeResponse ->
                exchangeResponse.cur_unit().equals(currency.toUpperCase()))
            .findFirst()
            // 데이터가 없으면 정해진 값을 반환함
            .orElse(new ExchangeResponse("USD", "미국 달러", "2000"));
    }
}
