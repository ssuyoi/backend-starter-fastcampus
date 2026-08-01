package com.github.ssuyoi.projectvoucher.app.controller.request;

public record EmployeeCreateRequest(
    String name,
    String position,
    String department) {

}
