package com.backendstarter.threadboard.repository;

import com.backendstarter.threadboard.model.entity.PostEntity;
import com.backendstarter.threadboard.model.entity.UserEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {


    List<PostEntity> findByUser(UserEntity user);
}
