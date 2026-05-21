package com.backendstarter.threadboard.service;

import com.backendstarter.threadboard.exception.post.PostNotFoundException;
import com.backendstarter.threadboard.exception.user.UserNotAllowedException;
import com.backendstarter.threadboard.exception.user.UserNotFoundException;
import com.backendstarter.threadboard.model.entity.LikeEntity;
import com.backendstarter.threadboard.model.entity.PostEntity;
import com.backendstarter.threadboard.model.entity.UserEntity;
import com.backendstarter.threadboard.model.post.Post;
import com.backendstarter.threadboard.model.post.PostPatchRequestBody;
import com.backendstarter.threadboard.model.post.PostPostRequestBody;
import com.backendstarter.threadboard.repository.LikeEntityRepository;
import com.backendstarter.threadboard.repository.PostEntityRepository;
import com.backendstarter.threadboard.repository.UserEntityRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PostService {

    @Autowired
    private PostEntityRepository postEntityRepository;
    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private LikeEntityRepository likeEntityRepository;

    private static final List<Post> posts = new ArrayList<>();

    public List<Post> getPosts() {
        var postEntities = postEntityRepository.findAll();

        //서비스에 실제로 필요한 데이터만 DTO로
        return postEntities.stream().map(Post::from).toList();
    }

    public Post getPostByPostId(Long postId) {
        var postEntity = postEntityRepository
            .findById(postId)
            .orElseThrow(
                () -> new PostNotFoundException(postId));

        return Post.from(postEntity);
    }


    public Post createPost(PostPostRequestBody postPostRequestBody, UserEntity currentUser) {
        var postEntity = postEntityRepository.save(
            PostEntity.of(postPostRequestBody.body(), currentUser)
        );
        return Post.from(postEntity);
    }

    public Post updatePost(Long postId, PostPatchRequestBody postPatchRequestBody,
        UserEntity currentUser) {
        var postEntity = postEntityRepository
            .findById(postId)
            .orElseThrow(
                () -> new PostNotFoundException());

        //작성자와 사용자 일치 확인
        if (!postEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        postEntity.setBody(postPatchRequestBody.body());
        var savedPostEntity = postEntityRepository.save(postEntity);

        return Post.from(savedPostEntity);
    }

    public void deletePost(Long postId, UserEntity currentUser) {
        var postEntity = postEntityRepository
            .findById(postId)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        //작성자와 사용자 일치 확인
        if (!postEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        postEntityRepository.delete(postEntity);
    }

    public List<Post> getPostsByUsername(String username) {

        var userEntity = userEntityRepository
            .findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(username));

        var postEntities = postEntityRepository.findByUser(userEntity);

        return postEntities.stream().map(Post::from).toList();
    }

    @Transactional
    public Post toggleLike(Long postId, UserEntity currentUser) {

        var postEntity = postEntityRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));

        var likeEntity = likeEntityRepository.findByUserAndPost(currentUser, postEntity);

        if (likeEntity.isPresent()) {
            likeEntityRepository.delete(likeEntity.get());
            postEntity.setLikeCount(Math.max(0, postEntity.getLikeCount() - 1));
        } else {
            likeEntityRepository.save(LikeEntity.of(currentUser, postEntity));
            postEntity.setLikeCount(postEntity.getLikeCount() + 1);
        }

        return Post.from(postEntityRepository.save(postEntity));
    }
}
