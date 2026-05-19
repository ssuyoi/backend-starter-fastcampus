package com.backendstarter.threadboard.service;

import com.backendstarter.threadboard.exception.post.PostNotFoundException;
import com.backendstarter.threadboard.exception.reply.ReplyNotFoundException;
import com.backendstarter.threadboard.exception.user.UserNotAllowedException;
import com.backendstarter.threadboard.model.entity.PostEntity;
import com.backendstarter.threadboard.model.entity.ReplyEntity;
import com.backendstarter.threadboard.model.entity.UserEntity;
import com.backendstarter.threadboard.model.reply.Reply;
import com.backendstarter.threadboard.model.reply.ReplyPatchRequestBody;
import com.backendstarter.threadboard.model.reply.ReplyPostRequestBody;
import com.backendstarter.threadboard.repository.PostEntityRepository;
import com.backendstarter.threadboard.repository.ReplyEntityRepository;
import com.backendstarter.threadboard.repository.UserEntityRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReplyService {

    @Autowired
    private ReplyEntityRepository replyEntityRepository;
    @Autowired
    private PostEntityRepository postEntityRepository;
    @Autowired
    private UserEntityRepository userEntityRepository;

    public List<Reply> getRepliesByPostId(Long postId) {
        var postEntity = postEntityRepository
            .findById(postId)
            .orElseThrow(
                () -> new PostNotFoundException(postId));

        var replyEntities = replyEntityRepository.findByPost(postEntity);
        return replyEntities.stream().map(Reply::from).toList();
    }

    @Transactional
    public Reply createReply(Long postId, ReplyPostRequestBody replyPostRequestBody,
        UserEntity currentUser) {
        var postEntity = postEntityRepository
            .findById(postId)
            .orElseThrow(
                () -> new PostNotFoundException());

        var replyEntity = replyEntityRepository.save(
            ReplyEntity.of(replyPostRequestBody.body(), currentUser, postEntity));

        postEntity.setRepliesCount(postEntity.getRepliesCount() + 1);

        return Reply.from(replyEntity);
    }


    public Reply updateReply(Long postId, Long replyId,
        ReplyPatchRequestBody replyPatchRequestBody, UserEntity currentUser) {
        postEntityRepository
            .findById(postId)
            .orElseThrow(
                () -> new PostNotFoundException(postId));

        var replyEntity = replyEntityRepository.findById(replyId)
            .orElseThrow(() -> new ReplyNotFoundException(replyId));

        if (!replyEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        replyEntity.setBody(replyPatchRequestBody.body());
        var savedReplyEntity = replyEntityRepository.save(replyEntity);

        return Reply.from(savedReplyEntity);
    }

    @Transactional
    public void deleteReply(Long postId, Long replyId, UserEntity currentUser) {
        var postEntity = postEntityRepository
            .findById(postId)
            .orElseThrow(
                () -> new PostNotFoundException(postId));

        var replyEntity = replyEntityRepository.findById(replyId)
            .orElseThrow(() -> new ReplyNotFoundException(replyId));

        if(!replyEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        replyEntityRepository.delete(replyEntity);

        postEntity.setRepliesCount(Math.max(0, postEntity.getRepliesCount() - 1));
        postEntityRepository.save(postEntity);
    }

}
