package com.backendstarter.threadboard.controller;

import com.backendstarter.threadboard.model.user.User;
import com.backendstarter.threadboard.model.user.UserSignUpRequestBody;
import com.backendstarter.threadboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<User> signUp(@RequestBody UserSignUpRequestBody userSignUpRequestBody) {

        var user = userService.signUp(userSignUpRequestBody.username(), userSignUpRequestBody.password());
        return ResponseEntity.ok(user);
    }

}
