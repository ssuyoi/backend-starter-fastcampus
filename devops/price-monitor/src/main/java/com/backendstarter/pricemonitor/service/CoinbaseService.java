package com.backendstarter.pricemonitor.service;

import com.backendstarter.pricemonitor.model.coinbase.SpotPriceResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CoinbaseService {

    private static final RestClient restClient = RestClient.create();

    public SpotPriceResponse getSpotPriceByCurrencyPair(String currencyPair) {
        return restClient
            .get()
            .uri("https://api.coinbase.com/v2/prices/{currencyPair}/spot", currencyPair)
            .retrieve()
            .body(SpotPriceResponse.class);
    }

}
