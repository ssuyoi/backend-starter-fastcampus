package com.backendstarter.threadboard.model.user;

import com.backendstarter.threadboard.model.entity.UserEntity;
import java.time.ZonedDateTime;

public record LikedUser(
    Long userId,
    String username,
    String profile,
    String description,
    Long followerCount,
    Long followingsCount,
    ZonedDateTime createdDateTime,
    ZonedDateTime updatedDateTime,
    Boolean isFollowing,
    Long likedPostId,
    ZonedDateTime likedDateTime) {

    public static LikedUser from(User user, Long likedPostId, ZonedDateTime likedDateTime) {
        return new LikedUser(
            user.userId(),
            user.username(),
            user.profile(),
            user.description(),
            user.followerCount(),
            user.followingsCount(),
            user.createdDateTime(),
            user.updatedDateTime(),
            user.isFollowing(),
            likedPostId,
            likedDateTime);
    }
}
