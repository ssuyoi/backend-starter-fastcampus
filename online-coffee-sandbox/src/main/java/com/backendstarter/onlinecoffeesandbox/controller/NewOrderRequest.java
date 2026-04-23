package com.backendstarter.onlinecoffeesandbox.controller;

import java.util.Map;
import lombok.Getter;

@Getter
public class NewOrderRequest {
    private final Integer customerId;
    private final Integer storeId;
    private final Map<Integer, Integer> products;

    public NewOrderRequest(Integer customerId, Integer storeId, Map<Integer, Integer> products) {
        this.customerId = customerId;
        this.storeId = storeId;
        this.products = products;
    }
}