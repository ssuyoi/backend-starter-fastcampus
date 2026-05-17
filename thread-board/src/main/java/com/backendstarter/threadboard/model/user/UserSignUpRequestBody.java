package com.backendstarter.threadboard.model.user;

public record UserSignUpRequestBody(
    String username,
    String password
) {

}