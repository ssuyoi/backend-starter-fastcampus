package com.backendstarter.threadboard.repository;

import com.backendstarter.threadboard.model.entity.LikeEntity;
import com.backendstarter.threadboard.model.entity.PostEntity;
import com.backendstarter.threadboard.model.entity.ReplyEntity;
import com.backendstarter.threadboard.model.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeEntityRepository extends JpaRepository<LikeEntity, Long> {

    List<LikeEntity> findByUser(UserEntity user);

    List<LikeEntity> findByPost(PostEntity post);

    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);
}
