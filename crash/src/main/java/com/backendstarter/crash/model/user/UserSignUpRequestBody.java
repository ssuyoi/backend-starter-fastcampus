package com.backendstarter.crash.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UserSignUpRequestBody(
    @NotEmpty String username,
    @NotEmpty String password,
    @NotEmpty String name,
    // Email 자동 검증 -> spring-boot-starter-validation
    @NotEmpty @Email String email
) {
}
