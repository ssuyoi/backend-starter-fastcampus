package com.backendstarter.threadboard.repository;

import com.backendstarter.threadboard.model.entity.PostEntity;
import com.backendstarter.threadboard.model.entity.ReplyEntity;
import com.backendstarter.threadboard.model.entity.UserEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyEntityRepository extends JpaRepository<ReplyEntity, Long> {

    List<ReplyEntity> findByUser(UserEntity user);

    List<ReplyEntity> findByPost(PostEntity post);

}
