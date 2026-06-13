package com.backendstarter.crash.model.registration;

import com.backendstarter.crash.model.crashsession.CrashSession;
import com.backendstarter.crash.model.entity.RegistrationEntity;
import com.backendstarter.crash.model.user.User;

public record Registration(
    Long registrationId,
    User user,
    CrashSession session
) {

    public static Registration from(RegistrationEntity registrationEntity) {
        return new Registration(
            registrationEntity.getRegistrationId(),
            User.from(registrationEntity.getUser()),
            CrashSession.from(registrationEntity.getSession())
        );
    }

}
