package com.backendstarter.onlinecoffeesandbox.repository;

import com.backendstarter.onlinecoffeesandbox.domain.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Integer> {

}
