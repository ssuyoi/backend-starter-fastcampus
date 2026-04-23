package com.backendstarter.onlinecoffeesandbox.domain;

import java.time.ZonedDateTime;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table(name="stores")
public class Store {

    @Id
    private int storeId;

    @Column
    private String name;

    @Column
    private String address;

    @Column
    private String phoneNumber;

    @Column
    private String openAt; // 0900

    @Column
    private String closeAt;
}
