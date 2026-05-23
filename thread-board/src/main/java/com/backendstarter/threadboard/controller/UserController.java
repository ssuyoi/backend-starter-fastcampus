package com.backendstarter.threadboard.controller;

import com.backendstarter.threadboard.model.entity.UserEntity;
import com.backendstarter.threadboard.model.post.Post;
import com.backendstarter.threadboard.model.user.User;
import com.backendstarter.threadboard.model.user.UserAuthenticationResponse;
import com.backendstarter.threadboard.model.user.UserLoginRequestBody;
import com.backendstarter.threadboard.model.user.UserPatchRequestBody;
import com.backendstarter.threadboard.model.user.UserSignUpRequestBody;
import com.backendstarter.threadboard.service.PostService;
import com.backendstarter.threadboard.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    PostService postService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String query) {
        var users = userService.getUsers(query);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        var user = userService.getUser(username);

        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{username}")
    public ResponseEntity<User> updateUser(@PathVariable String username,
        @RequestBody UserPatchRequestBody requestbody,
        Authentication authentication) {
        var user = userService.updateUser(username, requestbody,
            (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{username}/posts")
    public ResponseEntity<List<Post>> getPostsByUsername(@PathVariable String username, Authentication authentication) {
        var posts = postService.getPostsByUsername(username, (UserEntity)authentication.getPrincipal());

        return ResponseEntity.ok(posts);
    }

    @PostMapping("/{username}/follows")
    public ResponseEntity<User> follow(@PathVariable String username,
        Authentication authentication) {
        var user = userService.follow(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{username}/unfollows")
    public ResponseEntity<User> unfollow(@PathVariable String username,
        Authentication authentication) {
        var user = userService.unfollow(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<User>> getFollowersByUser(@PathVariable String username) {
        var followers = userService.getFollowersByUsername(username);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{username}/followings")
    public ResponseEntity<List<User>> getFollowingsByUser(@PathVariable String username) {
        var followings = userService.getFollowingsByUsername(username);
        return ResponseEntity.ok(followings);
    }


    @PostMapping
    public ResponseEntity<User> signUp(
        @Valid @RequestBody UserSignUpRequestBody userSignUpRequestBody) {

        var user = userService.signUp(userSignUpRequestBody.username(),
            userSignUpRequestBody.password());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticationResponse> authenticate(
        @Valid @RequestBody UserLoginRequestBody userLoginRequestBody) {

        var response = userService.authenticate(userLoginRequestBody.username(),
            userLoginRequestBody.password());
        return ResponseEntity.ok(response);
    }

}
