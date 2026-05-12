package com.backendstarter.threadboard.model;

import com.backendstarter.threadboard.model.entity.PostEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.ZonedDateTime;

//기존 Post 역할 -> PostEntity에 위임
//PostDTO로 변경
@JsonInclude(JsonInclude.Include.NON_NULL) //Null이 아닌 경우에만 json에 포함
public record Post(
    Long postId,
    String body,
    ZonedDateTime createdDateTime,
    ZonedDateTime updatedDateTime,
    ZonedDateTime deletedDateTime) {

    public static Post from(PostEntity postEntity) {
        return new Post(
            postEntity.getPostId(),
            postEntity.getBody(),
            postEntity.getCreatedDateTime(),
            postEntity.getUpdatedDateTime(),
            postEntity.getDeletedDateTime());
    }
}
