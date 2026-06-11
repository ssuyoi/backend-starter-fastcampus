package com.backendstarter.crash.model.sessionspeaker;

// 부분 수정이 있을 수 있기 때문에 PostRequestBody 레코드와 달리 @NotEmpty 어노테이션 제거
public record SessionSpeakerPatchRequestBody(
    String company,
    String name,
    String description
) {

}
