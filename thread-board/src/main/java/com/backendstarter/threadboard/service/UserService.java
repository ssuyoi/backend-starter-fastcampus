package com.backendstarter.threadboard.service;

import com.backendstarter.threadboard.exception.user.UserAlreadyExistsException;
import com.backendstarter.threadboard.exception.user.UserNotFoundException;
import com.backendstarter.threadboard.model.entity.UserEntity;
import com.backendstarter.threadboard.model.user.User;
import com.backendstarter.threadboard.model.user.UserAuthenticationResponse;
import com.backendstarter.threadboard.repository.UserEntityRepository;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //var userEntity = userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return userEntityRepository
            .findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(username));
    }

    public User signUp(String username, String password) {
        userEntityRepository
            .findByUsername(username)
            .ifPresent(user -> {
                throw new UserAlreadyExistsException();
            });

        var userEntity = UserEntity.of(username, passwordEncoder.encode(password));
        var savedUserEntity = userEntityRepository.save(userEntity);

        return User.from(savedUserEntity);
    }

    public UserAuthenticationResponse authenticate(@NotEmpty String username,
        @NotEmpty String password) {
        var userEntity = userEntityRepository
            .findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(username));

        if (passwordEncoder.matches(password, userEntity.getPassword())) {
            //패스워드 일치 -> jwt accessToken 발급
            var accessToken = jwtService.generateAccessToken(userEntity);
            return new UserAuthenticationResponse(accessToken);
        } else {
            throw new UserNotFoundException();
        }
    }
}
