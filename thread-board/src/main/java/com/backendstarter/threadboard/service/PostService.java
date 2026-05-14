package com.backendstarter.threadboard.service;

import com.backendstarter.threadboard.exception.post.PostNotFoundException;
import com.backendstarter.threadboard.model.Post;
import com.backendstarter.threadboard.model.PostPatchRequestBody;
import com.backendstarter.threadboard.model.PostPostRequestBody;
import com.backendstarter.threadboard.model.entity.PostEntity;
import com.backendstarter.threadboard.repository.PostEntityRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PostService {

    @Autowired
    private PostEntityRepository postEntityRepository;

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


    public Post createPost(PostPostRequestBody postPostRequestBody) {
        var postEntity = new PostEntity();
        postEntity.setBody(postPostRequestBody.body());
        var savedPostEntity = postEntityRepository.save(postEntity);

        return Post.from(savedPostEntity);
    }

    public Post updatePost(Long postId, PostPatchRequestBody postPatchRequestBody) {
        var postEntity = postEntityRepository
            .findById(postId)
            .orElseThrow(
                () -> new PostNotFoundException());
        postEntity.setBody(postPatchRequestBody.body());
        var savedPostEntity = postEntityRepository.save(postEntity);

        return Post.from(savedPostEntity);
    }

    public void deletePost(Long postId) {
        var postEntity = postEntityRepository
            .findById(postId)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        postEntityRepository.delete(postEntity);
    }
}
