package com.backendstarter.threadboard.service;

import com.backendstarter.threadboard.exception.user.UserNotFoundException;
import com.backendstarter.threadboard.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserEntityRepository userEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //var userEntity = userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return userEntityRepository
            .findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(username));
    }
}
