package com.machy.controller;

import com.machy.dto.response.ApiResponse;
import com.machy.exception.ResourceNotFoundException;
import com.machy.security.JwtAuthenticationFilter;
import com.machy.websocket.ScanSessionHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/scan")
public class ScanController {

    private final ScanSessionHandler scanSessionHandler;

    public ScanController(ScanSessionHandler scanSessionHandler) {
        this.scanSessionHandler = scanSessionHandler;
    }

    @PostMapping("/session")
    public ResponseEntity<ApiResponse<?>> createSession(Authentication auth, HttpServletRequest request) {
        var principal = (JwtAuthenticationFilter.UserPrincipal) auth.getPrincipal();
        String sessionId = generarIdSesion();
        String pin = String.format("%04d", (int) (Math.random() * 9000 + 1000));

        scanSessionHandler.createSession(sessionId, pin);

        String baseUrl = request.getRequestURL().toString().replace("/api/scan/session", "");
        String scanUrl = baseUrl + "remote-scan.html?session=" + sessionId;
        String qrUrl = "https://api.qrserver.com/v1/create-qr-code/?size=220x220&data=" +
                java.net.URLEncoder.encode(scanUrl, java.nio.charset.StandardCharsets.UTF_8);

        return ResponseEntity.ok(ApiResponse.ok(Map.of(
                "sessionId", sessionId,
                "pin", pin,
                "scanUrl", scanUrl,
                "qrUrl", qrUrl
        )));
    }

    @PostMapping("/session/{sessionId}/verify-pin")
    public ResponseEntity<ApiResponse<?>> verifyPin(@PathVariable String sessionId,
                                                     @RequestBody Map<String, String> body) {
        String pin = body.get("pin");
        if (pin == null || pin.length() != 4) {
            return ResponseEntity.badRequest().body(ApiResponse.error("PIN inválido"));
        }
        boolean valid = scanSessionHandler.verifyPin(sessionId, pin);
        if (valid) {
            return ResponseEntity.ok(ApiResponse.ok(Map.of("authenticated", true, "message", "PIN correcto")));
        } else {
            return ResponseEntity.ok(ApiResponse.ok(Map.of("authenticated", false, "message", "PIN incorrecto")));
        }
    }

    @PostMapping("/session/{sessionId}/code")
    public ResponseEntity<ApiResponse<?>> submitCode(@PathVariable String sessionId,
                                                      @RequestBody Map<String, String> body) {
        if (!scanSessionHandler.isAuthenticated(sessionId)) {
            return ResponseEntity.status(401).body(ApiResponse.error("Sesión no autenticada"));
        }
        String code = body.get("code");
        if (code == null || code.isBlank()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Código requerido"));
        }
        scanSessionHandler.submitCode(sessionId, code);
        return ResponseEntity.ok(ApiResponse.ok("Código recibido", Map.of("code", code)));
    }

    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<ApiResponse<?>> endSession(@PathVariable String sessionId, Authentication auth) {
        if (!scanSessionHandler.hasSession(sessionId)) {
            throw new ResourceNotFoundException("Sesión de escaneo", "sessionId", sessionId);
        }
        scanSessionHandler.removeSession(sessionId);
        return ResponseEntity.ok(ApiResponse.ok("Sesión finalizada", null));
    }

    private String generarIdSesion() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return sb.toString();
    }
}
