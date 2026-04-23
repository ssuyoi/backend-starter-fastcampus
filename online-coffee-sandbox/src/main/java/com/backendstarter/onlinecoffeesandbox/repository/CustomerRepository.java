package com.backendstarter.onlinecoffeesandbox.repository;

import com.backendstarter.onlinecoffeesandbox.domain.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
}
