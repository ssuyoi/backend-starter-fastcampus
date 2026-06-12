package com.backendstarter.crash.controller;

import com.backendstarter.crash.model.crashsession.CrashSession;
import com.backendstarter.crash.model.crashsession.CrashSessionPatchRequestBody;
import com.backendstarter.crash.model.crashsession.CrashSessionPostRequestBody;
import com.backendstarter.crash.model.sessionspeaker.SessionSpeaker;
import com.backendstarter.crash.model.sessionspeaker.SessionSpeakerPatchRequestBody;
import com.backendstarter.crash.model.sessionspeaker.SessionSpeakerPostRequestBody;
import com.backendstarter.crash.service.CrashSessionService;
import com.backendstarter.crash.service.SessionSpeakerService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/crash-sessions")
public class CrashSessionController {

    @Autowired private CrashSessionService crashSessionService;

    @GetMapping()
    public ResponseEntity<List<CrashSession>> getCrashSessions() {
        var crashSessions = crashSessionService.getCrashSessions();
        return ResponseEntity.ok(crashSessions);
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<CrashSession> getCrashSessionBySessionId(
        @PathVariable Long sessionId
    ) {
        var crashSession = crashSessionService.getCrashSessionBySessionId(sessionId);
        return ResponseEntity.ok(crashSession);
    }

    @PostMapping
    public ResponseEntity<CrashSession> createCrashSession(
        @Valid @RequestBody CrashSessionPostRequestBody crashSessionPostRequestBody
    ) {
        var crashSession = crashSessionService.createCrashSession(crashSessionPostRequestBody);
        return ResponseEntity.ok(crashSession);
    }

    @PatchMapping("/{sessionId}")
    public ResponseEntity<CrashSession> updateSessionSpeaker(
        @PathVariable Long sessionId,
        @RequestBody CrashSessionPatchRequestBody crashSessionPatchRequestBody
    ) {
        var crashSession = crashSessionService.updateCrashSession(sessionId, crashSessionPatchRequestBody);
        return ResponseEntity.ok(crashSession);
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteCrashSession(
        @PathVariable Long sessionId
    ) {
        crashSessionService.deleteCrashSession(sessionId);
        return ResponseEntity.noContent().build();
    }


}
