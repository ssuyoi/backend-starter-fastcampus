package com.backendstarter.crash.service;

import com.backendstarter.crash.exception.crashsession.CrashSessionNotFoundException;
import com.backendstarter.crash.model.crashsession.CrashSession;
import com.backendstarter.crash.model.crashsession.CrashSessionPatchRequestBody;
import com.backendstarter.crash.model.crashsession.CrashSessionPostRequestBody;
import com.backendstarter.crash.model.entity.CrashSessionEntity;
import com.backendstarter.crash.repository.CrashSessionCacheRepository;
import com.backendstarter.crash.repository.CrashSessionEntityRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class CrashSessionService {

    @Autowired
    private CrashSessionEntityRepository crashSessionEntityRepository;
    @Autowired
    private CrashSessionCacheRepository crashSessionCacheRepository;
    @Autowired
    private SessionSpeakerService sessionSpeakerService;

    /**
     * 저장된 모든 CrashSession 조회
     *
     * @return 전체 {@link CrashSession} 목록
     */
    public List<CrashSession> getCrashSessions() {
        var crashSessions = crashSessionCacheRepository.getCrashSessionsListCache();
        if (!ObjectUtils.isEmpty(crashSessions)) {
            return crashSessions;
        } else {
            var crashSessionsList = crashSessionEntityRepository.findAll().stream().map(CrashSession::from).toList();
            crashSessionCacheRepository.setCrashSessionsListCache(crashSessionsList);
            return crashSessionsList;
        }
    }

    /**
     * sessionId에 해당하는 CrashSession 조회
     *
     * @param sessionId 조회할 세션의 ID
     * @return 조회된 {@link CrashSession} 도메인 객체
     * @throws com.backendstarter.crash.exception.crashsession.CrashSessionNotFoundException 해당 sessionId가 존재하지 않을 경우
     */
    public CrashSession getCrashSessionBySessionId(Long sessionId) {
        return crashSessionCacheRepository
            .getCrashSessionCache(sessionId)
            .orElseGet(
                () -> {
                    var crashSessionEntity = getCrashSessionEntityBySessionId(sessionId);
                    var crashSession = CrashSession.from(crashSessionEntity);
                    crashSessionCacheRepository.setCrashSessionCache(crashSession);
                    return crashSession;
                }
            );
    }

    /**
     * 새로운 CrashSession 생성
     * <p>
     * sessionSpeaker는 요청 바디의 speakerId로 {@link SessionSpeakerService}에서 조회한
     * SessionSpeakerEntity를 CrashSessionEntity에 연결해 저장
     * </p>
     *
     * @param crashSessionPostRequestBody 세션 생성에 필요한 요청 데이터
     * @return 생성된 {@link CrashSession} 도메인 객체
     */
    public CrashSession createCrashSession(
        CrashSessionPostRequestBody crashSessionPostRequestBody) {

        var sessionSpeakerEntity =
            sessionSpeakerService.getSessionSpeakerEntityBySpeakerId(
                crashSessionPostRequestBody.speakerId());

        var crashSessionEntity =
            CrashSessionEntity.of(
                crashSessionPostRequestBody.title(),
                crashSessionPostRequestBody.body(),
                crashSessionPostRequestBody.category(),
                crashSessionPostRequestBody.dateTime(),
                sessionSpeakerEntity);

        return CrashSession.from(crashSessionEntityRepository.save(crashSessionEntity));
    }


    /**
     * 기존 CrashSession 부분 수정
     * <p>
     * {@link CrashSessionPatchRequestBody} 내 각 필드가 비어 있지 않은 경우에만
     * 해당 필드를 setter로 업데이트 (Partial Update)
     * </p>
     *
     * @param sessionId                    수정할 세션의 ID
     * @param crashSessionPatchRequestBody 수정할 필드가 담긴 요청 데이터
     * @return 수정된 {@link CrashSession} 도메인 객체
     * @throws com.backendstarter.crash.exception.crashsession.CrashSessionNotFoundException 해당 sessionId가 존재하지 않을 경우
     */
    public CrashSession updateCrashSession(
        Long sessionId, CrashSessionPatchRequestBody crashSessionPatchRequestBody) {

        var crashSessionEntity = getCrashSessionEntityBySessionId(sessionId);

        if(!ObjectUtils.isEmpty(crashSessionPatchRequestBody.title())) {
            crashSessionEntity.setTitle(
                crashSessionPatchRequestBody.title()
            );
        }
        if(!ObjectUtils.isEmpty(crashSessionPatchRequestBody.body())) {
            crashSessionEntity.setBody(
                crashSessionPatchRequestBody.body()
            );
        }
        if(!ObjectUtils.isEmpty(crashSessionPatchRequestBody.category())) {
            crashSessionEntity.setCategory(
                crashSessionPatchRequestBody.category()
            );
        }
        if(!ObjectUtils.isEmpty(crashSessionPatchRequestBody.dateTime())) {
            crashSessionEntity.setDateTime(
                crashSessionPatchRequestBody.dateTime()
            );
        }
        if(!ObjectUtils.isEmpty(crashSessionPatchRequestBody.speakerId())) {
            var sessionSpeakerEntity =
                sessionSpeakerService.getSessionSpeakerEntityBySpeakerId(
                    crashSessionPatchRequestBody.speakerId());
            crashSessionEntity.setSpeaker(sessionSpeakerEntity);
        }

        return CrashSession.from(crashSessionEntityRepository.save(crashSessionEntity));
    }

    /**
     * CrashSession 삭제
     * <p>
     * sessionId로 CrashSessionEntity를 먼저 조회한 뒤 삭제
     * 존재하지 않는 sessionId가 전달되면 예외 발생
     * </p>
     *
     * @param sessionId 삭제할 세션의 ID
     * @throws com.backendstarter.crash.exception.crashsession.CrashSessionNotFoundException 해당 sessionId가 존재하지 않을 경우
     */
    public void deleteCrashSession(Long sessionId) {
        var crashSessionEntity = getCrashSessionEntityBySessionId(sessionId);
        crashSessionEntityRepository.delete(crashSessionEntity);
    }

    /**
     * sessionId에 해당하는 {@link CrashSessionEntity} 조회
     * <p>
     * 서비스 내부에서 공통으로 사용하는 Entity 조회 메소드
     * 존재하면 Entity를 반환하고, 없으면 {@link CrashSessionNotFoundException}을 발생
     * </p>
     *
     * @param sessionId 조회할 세션의 ID
     * @return 조회된 {@link CrashSessionEntity}
     * @throws CrashSessionNotFoundException 해당 sessionId가 존재하지 않을 경우
     */
    public CrashSessionEntity getCrashSessionEntityBySessionId(Long sessionId) {
        return crashSessionEntityRepository.findById(sessionId).orElseThrow(
            () -> new CrashSessionNotFoundException(sessionId)
        );
    }
}
