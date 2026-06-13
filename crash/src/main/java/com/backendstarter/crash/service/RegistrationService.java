package com.backendstarter.crash.service;

import com.backendstarter.crash.exception.registration.RegistrationAlreadyExistException;
import com.backendstarter.crash.exception.registration.RegistrationNotFoundException;
import com.backendstarter.crash.model.crashsession.CrashSessionRegistrationStatus;
import com.backendstarter.crash.model.entity.RegistrationEntity;
import com.backendstarter.crash.model.entity.UserEntity;
import com.backendstarter.crash.model.registration.Registration;
import com.backendstarter.crash.model.registration.RegistrationPostRequestBody;
import com.backendstarter.crash.repository.RegistrationEntityRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationEntityRepository registrationEntityRepository;

    @Autowired
    private CrashSessionService crashSessionService;


    /**
     * 현재 로그인한 사용자의 신청 목록 반환
     */
    public List<Registration> getRegistrationsByCurrentUser(UserEntity currentUser) {
        var registrationEntities = registrationEntityRepository.findByUser(currentUser);
        return registrationEntities.stream().map(Registration::from).toList();

    }

    /**
     * 현재 로그인한 사용자의 특정 신청 반환
     */
    public Registration getRegistrationByRegistrationIdByCurrentUser(Long registrationId,
        UserEntity currentUser) {
        var registrationEntity = getRegistrationEntityByRegistrationIdAndUserEntity(registrationId,
            currentUser);
        return Registration.from(registrationEntity);
    }

    /**
     * 현재 로그인한 사용자로 세션 신청 생성.
     * 중복 신청 시 {@link RegistrationAlreadyExistException} 발생
     */
    public Registration createRegistrationByCurrentUser(
        RegistrationPostRequestBody registrationPostRequestBody, UserEntity currentUser) {
        var crashSessionEntity =
            crashSessionService.getCrashSessionEntityBySessionId(
                registrationPostRequestBody.sessionId());

        registrationEntityRepository
            .findByUserAndSession(currentUser, crashSessionEntity)
            .ifPresent(registrationEntity -> {
                throw new RegistrationAlreadyExistException(registrationEntity.getRegistrationId(),
                    currentUser);
            });

        var registrationEntity = RegistrationEntity.of(currentUser, crashSessionEntity);
        return Registration.from(
            registrationEntityRepository.save(registrationEntity)
        );
    }

    /**
     * 현재 로그인한 사용자의 특정 신청 삭제
     */
    public void deleteRegistrationByRegistrationIdAndCurrentUser(Long registrationId,
        UserEntity currentUser) {
        var registrationEntity = getRegistrationEntityByRegistrationIdAndUserEntity(registrationId,
            currentUser);
        registrationEntityRepository.delete(registrationEntity);

    }

    /**
     * 사용자와 신청 ID로 엔티티 반환. 없으면 {@link RegistrationNotFoundException} 발생
     */
    public RegistrationEntity getRegistrationEntityByRegistrationIdAndUserEntity(
        Long registrationId, UserEntity userEntity) {
        return registrationEntityRepository.findByRegistrationIdAndUser(registrationId, userEntity)
            .orElseThrow(
                () -> new RegistrationNotFoundException(registrationId, userEntity)
            );
    }

    /**
     * 현재 로그인한 사용자의 특정 세션 신청 여부 반환
     */
    public CrashSessionRegistrationStatus getCrashSessionRegistrationStatusBySessionIdAndCurrentUser(
        Long sessionId, UserEntity currentUser
    ) {
        var crashSessionEntity = crashSessionService.getCrashSessionEntityBySessionId(sessionId);
        var registrationEntity =
            registrationEntityRepository.findByUserAndSession(currentUser, crashSessionEntity);
        return new CrashSessionRegistrationStatus(
            sessionId,
            registrationEntity.isPresent(),
            registrationEntity.map(RegistrationEntity::getRegistrationId).orElse(null)
        );
    }
}
