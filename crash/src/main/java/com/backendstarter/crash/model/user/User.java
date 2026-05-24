package com.backendstarter.crash.model.user;

import com.backendstarter.crash.model.entity.UserEntity;

//UserDTO
public record User(Long userId, String username, String name, String email) {

    public static User from(UserEntity userEntity) {
        return new User(
            userEntity.getUserId(),
            userEntity.getUsername(),
            userEntity.getName(),
            userEntity.getEmail()
        );
    }
}
