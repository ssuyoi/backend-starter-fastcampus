package com.backendstarter.pricemonitor.model.price;

import com.backendstarter.pricemonitor.model.coinbase.SpotPriceResponse;

public record PriceResponse(Double amount, String base, String currency) {

    public static PriceResponse from(SpotPriceResponse spotPriceResponse) {
        var mount = Double.parseDouble(spotPriceResponse.data().amount());
        var base = spotPriceResponse.data().base();
        var currency = spotPriceResponse.data().currency();

        return new PriceResponse(mount, base, currency);
    }
}
