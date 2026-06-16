package com.backendstarter.crash.service;

import com.backendstarter.crash.exception.user.UserAlreadyExistsException;
import com.backendstarter.crash.exception.user.UserNotFoundException;
import com.backendstarter.crash.model.entity.UserEntity;
import com.backendstarter.crash.model.user.User;
import com.backendstarter.crash.model.user.UserAuthenticationResponse;
import com.backendstarter.crash.model.user.UserLoginRequestBody;
import com.backendstarter.crash.model.user.UserSignUpRequestBody;
import com.backendstarter.crash.repository.UserEntityCacheRepository;
import com.backendstarter.crash.repository.UserEntityRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired private UserEntityRepository userEntityRepository;
    @Autowired private UserEntityCacheRepository userEntityCacheRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserEntityByUsername(username);
    }
    /**
     * 이미 회원가입을 한 적이 있는지 검증 후
     * 있으면 UserAlreadyExistsException() 커스텀 예외
     */
    public User signUp(UserSignUpRequestBody userSignUpRequestBody) {
        userEntityRepository.findByUsername(userSignUpRequestBody.username())
            .ifPresent(
                user -> {
                    throw new UserAlreadyExistsException();
                }
            );
        var userEntity =
            userEntityRepository.save(
                UserEntity.of(
                    userSignUpRequestBody.username(),
                    passwordEncoder.encode(userSignUpRequestBody.password()),
                    userSignUpRequestBody.name(),
                    userSignUpRequestBody.email()

                )
            );
        return User.from(userEntity);
    }

    public UserAuthenticationResponse authenticate(
        @Valid UserLoginRequestBody userLoginRequestBody) {
        var userEntity = getUserEntityByUsername(userLoginRequestBody.username());

        if (passwordEncoder.matches(userLoginRequestBody.password(), userEntity.getPassword())) {
            var accessToken = jwtService.generateAccessToken(userEntity);
            return new UserAuthenticationResponse(accessToken);
        } else {
            throw new UserNotFoundException(userLoginRequestBody.username());
        }
    }

    private UserEntity getUserEntityByUsername(String username) {
        var userEntityCache = userEntityCacheRepository.getUserEntityCache(username);

        if (userEntityCache.isPresent()) {
            return userEntityCache.get();
        } else {
            var userEntity =
                userEntityRepository
                    .findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException(username));

            userEntityCacheRepository.setUserEntityCache(userEntity);

            return userEntity;
        }
    }
}
