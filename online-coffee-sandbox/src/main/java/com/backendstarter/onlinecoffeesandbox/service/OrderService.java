package com.backendstarter.onlinecoffeesandbox.service;

import com.backendstarter.onlinecoffeesandbox.domain.CreateOrder;
import com.backendstarter.onlinecoffeesandbox.domain.Order;
import com.backendstarter.onlinecoffeesandbox.domain.StoreProduct;
import com.backendstarter.onlinecoffeesandbox.repository.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final StoreService storeService;

    public OrderService(OrderRepository orderRepository, StoreService storeService) {
        this.orderRepository = orderRepository;
        this.storeService = storeService;
    }

    public void newOrder(CreateOrder createOrder) {
        List<StoreProduct> storeProducts = new ArrayList<>();
        for(Map.Entry<Integer, Integer> entry : createOrder.getQuantityByProduct().entrySet()) {
            Integer productId = entry.getKey();
            Integer buyQuantity = entry.getValue();

            StoreProduct storeProduct = storeService.getStoreProduct(
                createOrder.getStoreId(),
                productId
            );

            int stockQuantity = storeProduct.getStockQuantity();

            if (buyQuantity > stockQuantity) {
                throw new RuntimeException("재고가 없습니다.");
            }

            storeProduct.adjustStockQuantity(buyQuantity);
            storeProducts.add(storeProduct);
        }
        Order entity = Order.newOrder(createOrder);
        orderRepository.save(entity);
        storeService.saveAll(storeProducts);
    }
}
