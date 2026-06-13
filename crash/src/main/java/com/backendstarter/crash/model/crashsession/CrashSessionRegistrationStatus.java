package com.backendstarter.crash.model.crashsession;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

// registrationId는 user가 등록하지 않았다면 존재하지 않기 때문에 Include.NON_NULL 추가
@JsonInclude(Include.NON_NULL)
public record CrashSessionRegistrationStatus(
    Long sessionId,
    boolean isRegistered,
    Long registrationId
) {}
