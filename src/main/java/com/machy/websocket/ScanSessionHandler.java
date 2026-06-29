package com.machy.websocket;

import com.machy.entity.ScanSession;
import com.machy.repository.ScanSessionRepository;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class ScanSessionHandler {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ScanSessionHandler.class);

    private final SimpMessagingTemplate messaging;
    private final ScanSessionRepository scanSessionRepository;

    public ScanSessionHandler(SimpMessagingTemplate messaging, ScanSessionRepository scanSessionRepository) {
        this.messaging = messaging;
        this.scanSessionRepository = scanSessionRepository;
    }

    private final Map<String, String> sessionPins = new ConcurrentHashMap<>();
    private final Map<String, Boolean> authenticatedSessions = new ConcurrentHashMap<>();

    @MessageMapping("/scan.join.{sessionId}")
    public void joinSession(@DestinationVariable String sessionId, @Payload Map<String, String> payload) {
        String type = payload.get("type");
        String code = payload.get("code");

        if ("PIN".equals(type)) {
            String expectedPin = sessionPins.get(sessionId);
            if (expectedPin != null && expectedPin.equals(code)) {
                authenticatedSessions.put(sessionId, true);
                messaging.convertAndSend("/topic/scan." + sessionId,
                        Map.of("type", "AUTH_OK", "message", "Celular autenticado"));
                log.info("Session {} authenticated via PIN", sessionId);
            } else {
                messaging.convertAndSend("/topic/scan." + sessionId,
                        Map.of("type", "AUTH_FAIL", "message", "PIN incorrecto"));
                log.warn("Session {} PIN verification failed", sessionId);
            }
        }
    }

    @MessageMapping("/scan.code.{sessionId}")
    public void receiveCode(@DestinationVariable String sessionId, @Payload Map<String, String> payload) {
        if (!authenticatedSessions.getOrDefault(sessionId, false)) {
            messaging.convertAndSend("/topic/scan." + sessionId,
                    Map.of("type", "ERROR", "message", "No autenticado"));
            return;
        }

        String code = payload.get("code");
        if (code != null) {
            scanSessionRepository.save(ScanSession.builder()
                    .sessionId(sessionId)
                    .code(code)
                    .usuarioId("remote-scan")
                    .build());

            messaging.convertAndSend("/topic/scan." + sessionId,
                    Map.of("type", "CODE", "code", code));
            log.info("Code {} received for session {}", code, sessionId);
        }
    }

    public void createSession(String sessionId, String pin) {
        sessionPins.put(sessionId, pin);
        authenticatedSessions.put(sessionId, false);
        scanSessionRepository.save(ScanSession.builder()
                .sessionId(sessionId)
                .code(null)
                .usuarioId("pc-host")
                .build());
        log.info("Scan session created: {} PIN: {}", sessionId, pin);
    }

    public void removeSession(String sessionId) {
        sessionPins.remove(sessionId);
        authenticatedSessions.remove(sessionId);
        log.info("Scan session removed: {}", sessionId);
    }

    public boolean verifyPin(String sessionId, String pin) {
        String expectedPin = sessionPins.get(sessionId);
        if (expectedPin != null && expectedPin.equals(pin)) {
            authenticatedSessions.put(sessionId, true);
            messaging.convertAndSend("/topic/scan." + sessionId,
                    Map.of("type", "AUTH_OK", "message", "Celular autenticado"));
            log.info("Session {} authenticated via REST PIN", sessionId);
            return true;
        }
        messaging.convertAndSend("/topic/scan." + sessionId,
                Map.of("type", "AUTH_FAIL", "message", "PIN incorrecto"));
        log.warn("Session {} PIN verification failed via REST", sessionId);
        return false;
    }

    public void submitCode(String sessionId, String code) {
        scanSessionRepository.save(ScanSession.builder()
                .sessionId(sessionId)
                .code(code)
                .usuarioId("remote-scan")
                .build());
        messaging.convertAndSend("/topic/scan." + sessionId,
                Map.of("type", "CODE", "code", code));
        log.info("Code {} received for session {} via REST", code, sessionId);
    }

    public boolean hasSession(String sessionId) {
        return sessionPins.containsKey(sessionId);
    }

    public boolean isAuthenticated(String sessionId) {
        return authenticatedSessions.getOrDefault(sessionId, false);
    }
}
