package com.backendstarter.threadboard.controller;

import com.backendstarter.threadboard.model.entity.UserEntity;
import com.backendstarter.threadboard.model.post.Post;
import com.backendstarter.threadboard.model.reply.Reply;
import com.backendstarter.threadboard.model.user.Follower;
import com.backendstarter.threadboard.model.user.LikedUser;
import com.backendstarter.threadboard.model.user.User;
import com.backendstarter.threadboard.model.user.UserAuthenticationResponse;
import com.backendstarter.threadboard.model.user.UserLoginRequestBody;
import com.backendstarter.threadboard.model.user.UserPatchRequestBody;
import com.backendstarter.threadboard.model.user.UserSignUpRequestBody;
import com.backendstarter.threadboard.service.PostService;
import com.backendstarter.threadboard.service.ReplyService;
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
    @Autowired
    ReplyService replyService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String query,
        Authentication authentication) {
        var users = userService.getUsers(query, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username,
        Authentication authentication) {
        var user = userService.getUser(username, (UserEntity) authentication.getPrincipal());

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
    public ResponseEntity<List<Post>> getPostsByUsername(@PathVariable String username,
        Authentication authentication) {
        var posts = postService.getPostsByUsername(username,
            (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(posts);
    }

    @PostMapping("/{username}/follows")
    public ResponseEntity<User> follow(@PathVariable String username,
        Authentication authentication) {
        var user = userService.follow(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{username}/follows")
    public ResponseEntity<User> unfollow(@PathVariable String username,
        Authentication authentication) {
        var user = userService.unfollow(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<Follower>> getFollowersByUser(@PathVariable String username,
        Authentication authentication) {
        var followers = userService.getFollowersByUsername(username,
            (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{username}/followings")
    public ResponseEntity<List<User>> getFollowingsByUser(@PathVariable String username,
        Authentication authentication) {
        var followings = userService.getFollowingsByUsername(username,
            (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(followings);
    }

    @GetMapping("/{username}/replies")
    public ResponseEntity<List<Reply>> getRepliesByUser(@PathVariable String username) {
        var replies = replyService.getRepliesByUser(username);
        return ResponseEntity.ok(replies);
    }

    @GetMapping("/{username}/liked-users")
    public ResponseEntity<List<LikedUser>> getLikedUsersByUser(@PathVariable String username,
        Authentication authentication) {
        var likedUsers = userService.getLikedUsersByUser(username,
            (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(likedUsers);
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
