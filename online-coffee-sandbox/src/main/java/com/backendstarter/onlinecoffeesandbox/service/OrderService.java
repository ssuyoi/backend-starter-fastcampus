package com.backendstarter.onlinecoffeesandbox.service;

import com.backendstarter.onlinecoffeesandbox.domain.CreateOrder;
import com.backendstarter.onlinecoffeesandbox.domain.Order;
import com.backendstarter.onlinecoffeesandbox.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void newOrder(CreateOrder createOrder) {
        Order entity = Order.newOrder(createOrder);
        orderRepository.save(entity);
    }
}
