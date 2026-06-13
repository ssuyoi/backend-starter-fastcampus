package com.backendstarter.crash.exception.registration;

import com.backendstarter.crash.exception.ClientErrorException;
import com.backendstarter.crash.model.entity.UserEntity;
import org.springframework.http.HttpStatus;

public class RegistrationAlreadyExistException extends ClientErrorException {

    public RegistrationAlreadyExistException() {
        super(HttpStatus.CONFLICT, "Registration already exist.");
    }

    public RegistrationAlreadyExistException(Long registrationId, UserEntity userEntity) {
        super(HttpStatus.NOT_FOUND,
            "Registration with registrationId " + registrationId
                + " and name " + userEntity.getName() + " already exist.");
    }
}
