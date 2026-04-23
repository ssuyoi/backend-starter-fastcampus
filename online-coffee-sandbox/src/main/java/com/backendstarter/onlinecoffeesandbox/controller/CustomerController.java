package com.backendstarter.onlinecoffeesandbox.controller;

import com.backendstarter.onlinecoffeesandbox.domain.CreateCustomer;
import com.backendstarter.onlinecoffeesandbox.domain.CustomerDto;
import com.backendstarter.onlinecoffeesandbox.service.CustomerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * 신규 고객 등록
     *
     * @param name 이름
     * @param address 주소
     * @param phoneNumber 핸드폰번호
     * @return 등록된 고객 정보 (CustomerDto)
     */
    @PostMapping("/api/v1/customers")
    public Response<CustomerDto> createNewCustomer(
        @RequestParam String name,
        @RequestParam String address,
        @RequestParam String phoneNumber
    ) {
        return Response.success(customerService.newCustomer(
            CreateCustomer.builder()
                .name(name)
                .address(address)
                .phoneNumber(phoneNumber)
                .build()
            )
        );
    }
}
