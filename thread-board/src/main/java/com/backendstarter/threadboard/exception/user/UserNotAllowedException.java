package com.backendstarter.threadboard.exception.user;

import com.backendstarter.threadboard.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserNotAllowedException extends ClientErrorException {

    public UserNotAllowedException() {
        super(HttpStatus.FORBIDDEN, "User not allowed");
    }

}
