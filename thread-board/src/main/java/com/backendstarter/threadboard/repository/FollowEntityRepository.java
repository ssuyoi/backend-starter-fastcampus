package com.backendstarter.threadboard.repository;

import com.backendstarter.threadboard.model.entity.FollowEntity;
import com.backendstarter.threadboard.model.entity.LikeEntity;
import com.backendstarter.threadboard.model.entity.PostEntity;
import com.backendstarter.threadboard.model.entity.UserEntity;
import com.backendstarter.threadboard.model.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowEntityRepository extends JpaRepository<FollowEntity, Long> {

    List<FollowEntity> findByFollower(UserEntity follower);

    List<FollowEntity> findByFollowing(UserEntity following);

    Optional<FollowEntity> findByFollowerAndFollowing(UserEntity follower, UserEntity following);
}
