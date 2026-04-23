package com.backendstarter.onlinecoffeesandbox.controller;

import com.backendstarter.onlinecoffeesandbox.domain.CreateOrder;
import com.backendstarter.onlinecoffeesandbox.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 주문
     *
     * @param request 주문 요청 정보(customerId, storeId, products)
     * @return 주문 처리 결과
     */
    @PostMapping("/api/v1/orders")
    public Response<Void> newOrder(
        @RequestBody NewOrderRequest request
    ) {
        orderService.newOrder(CreateOrder.builder()
            .customerId(request.getCustomerId())
            .storeId(request.getStoreId())
            .quantityByProduct(request.getProducts())
            .build());
        return Response.success(null);
    }
}
