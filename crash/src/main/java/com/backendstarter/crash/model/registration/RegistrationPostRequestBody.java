package com.backendstarter.crash.model.registration;

import com.backendstarter.crash.model.crashsession.CrashSessionCategory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;

public record RegistrationPostRequestBody(
    @NotNull Long sessionId
) {}
