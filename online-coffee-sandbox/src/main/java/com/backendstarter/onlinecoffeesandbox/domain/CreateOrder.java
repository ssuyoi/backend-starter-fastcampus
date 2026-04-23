package com.backendstarter.onlinecoffeesandbox.domain;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateOrder {
    private int customerId;
    private Map<Integer, Integer> quantityByProduct;
}
