package com.backendstarter.crash.repository;

import com.backendstarter.crash.model.entity.CrashSessionEntity;
import com.backendstarter.crash.model.entity.RegistrationEntity;
import com.backendstarter.crash.model.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationEntityRepository extends JpaRepository<RegistrationEntity, Long> {

    List<RegistrationEntity> findByUser(UserEntity user);

    Optional<RegistrationEntity> findByRegistrationIdAndUser(Long registrationId, UserEntity user);

    // 이미 세션에 등록된 유저인지 확인하기 위해
    Optional<RegistrationEntity> findByUserAndSession(UserEntity user, CrashSessionEntity session);
}
