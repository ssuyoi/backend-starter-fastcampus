package com.backendstarter.threadboard.controller;

import com.backendstarter.threadboard.model.entity.UserEntity;
import com.backendstarter.threadboard.model.post.Post;
import com.backendstarter.threadboard.model.post.PostPatchRequestBody;
import com.backendstarter.threadboard.model.post.PostPostRequestBody;
import com.backendstarter.threadboard.model.user.LikedUser;
import com.backendstarter.threadboard.service.PostService;
import com.backendstarter.threadboard.service.UserService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(Authentication authentication) {
        logger.info("GET/ api/v1/posts");
        var posts = postService.getPosts((UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(posts);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostByPostId(@PathVariable Long postId,
        Authentication authentication) {
        logger.info("GET/api/v1/posts/{}", postId);
        var matchingPost = postService.getPostByPostId(postId,
            (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(matchingPost);
    }


    @GetMapping("/{postId}/liked-users")
    public ResponseEntity<List<LikedUser>> getLikedUsersByPostId(@PathVariable Long postId,
        Authentication authentication) {
        logger.info("GET/api/v1/posts/{}", postId);
        var likedUsers = userService.getLikedUsersByPostId(
            postId, (UserEntity) authentication.getPrincipal()
        );
        return ResponseEntity.ok(likedUsers);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostPostRequestBody postPostRequestBody,
        Authentication authentication) {
        logger.info("POST/api/v1/posts");

        //변수명만 봐도 타입 추론이 가능한 경우 자동으로 타입을 지정해주는 var를 사용하면 편리함
        var post = postService.createPost(postPostRequestBody,
            (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(post);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable Long postId,
        @RequestBody PostPatchRequestBody postPatchRequestBody, Authentication authentication) {
        logger.info("PATCH/api/v1/posts/{}", postId);
        var post = postService.updatePost(postId, postPatchRequestBody,
            (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(post);
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<Post> toggleLike(@PathVariable Long postId,
        Authentication authentication) {
        var post = postService.toggleLike(postId, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(post);
    }
}
