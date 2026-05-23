package com.backendstarter.threadboard.model.user;

import java.time.ZonedDateTime;

public record Follower(
    Long userId,
    String username,
    String profile,
    String description,
    Long followerCount,
    Long followingsCount,
    ZonedDateTime createdDateTime,
    ZonedDateTime updatedDateTime,
    ZonedDateTime followedDateTime,
    Boolean isFollowing) {

    public static Follower from(User user, ZonedDateTime followedDateTime) {
        return new Follower(
            user.userId(),
            user.username(),
            user.profile(),
            user.description(),
            user.followerCount(),
            user.followingsCount(),
            user.createdDateTime(),
            user.updatedDateTime(),
            followedDateTime,
            user.isFollowing());
    }
}
