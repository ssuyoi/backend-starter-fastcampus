package com.backendstarter.onlinecoffeesandbox.domain;

import lombok.Cleanup;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table(name="customers")
public class Customer {

    @Id
    private int customerId;

    @Column
    private String name;

    @Column
    private String address;

    @Column
    private String phoneNumber;

    public Customer(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    /*public static Customer newCustomer(String name, String address, String phoneNumber) {
        return new Customer(name, address, phoneNumber);
    }*/

    public static Customer newCustomer(CreateCustomer createCustomer) {
        return new Customer(createCustomer.getName(), createCustomer.getAddress(), createCustomer.getPhoneNumber());
    }
}
