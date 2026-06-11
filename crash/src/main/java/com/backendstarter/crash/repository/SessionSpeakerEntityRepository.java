package com.backendstarter.crash.repository;

import com.backendstarter.crash.model.entity.SessionSpeakerEntity;
import com.backendstarter.crash.model.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionSpeakerEntityRepository extends JpaRepository<SessionSpeakerEntity, Long> {}
