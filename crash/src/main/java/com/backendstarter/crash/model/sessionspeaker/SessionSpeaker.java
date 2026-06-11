package com.backendstarter.crash.model.sessionspeaker;

import com.backendstarter.crash.model.entity.SessionSpeakerEntity;

public record SessionSpeaker(
    Long speakerId,
    String company,
    String name,
    String description,
    String profile
) {

    // SessionSpeakerEntity -> SessionSpeaker
    // 엔티티를 레코드로 쉽게 변환할 수 있는 메서드 생성
    public static SessionSpeaker from(SessionSpeakerEntity sessionSpeakerEntity) {
        return new SessionSpeaker(
            sessionSpeakerEntity.getSpeakerId(),
            sessionSpeakerEntity.getCompany(),
            sessionSpeakerEntity.getName(),
            sessionSpeakerEntity.getDescription(),
            sessionSpeakerEntity.getProfile()
        );
    }
}
