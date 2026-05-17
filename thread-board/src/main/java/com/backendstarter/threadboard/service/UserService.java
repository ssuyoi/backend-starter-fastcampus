package com.backendstarter.threadboard.service;

import com.backendstarter.threadboard.exception.user.UserAlreadyExistsException;
import com.backendstarter.threadboard.exception.user.UserNotAllowedException;
import com.backendstarter.threadboard.exception.user.UserNotFoundException;
import com.backendstarter.threadboard.model.entity.UserEntity;
import com.backendstarter.threadboard.model.user.User;
import com.backendstarter.threadboard.model.user.UserAuthenticationResponse;
import com.backendstarter.threadboard.model.user.UserPatchRequestBody;
import com.backendstarter.threadboard.repository.UserEntityRepository;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
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

    public List<User> getUsers(String query) {
        List<UserEntity> userEntities;

        if (query != null && !query.isBlank()) {
            userEntities = userEntityRepository.findByUsernameContaining(query);
        } else {
            userEntities = userEntityRepository.findAll();
        }

        return userEntities.stream().map(User::from).toList();
    }

    public User getUser(String username) {
        var userEntity = userEntityRepository
            .findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(username));

        return User.from(userEntity);
    }

    public User updateUser(String username, UserPatchRequestBody userPatchRequestBody,
        UserEntity currentUser) {
        var userEntity = userEntityRepository
            .findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(username));

        if (!userEntity.equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        if (userPatchRequestBody.description() != null) {
            userEntity.setDescription(userPatchRequestBody.description());
        }

        return User.from(userEntityRepository.save(userEntity));
    }
}
