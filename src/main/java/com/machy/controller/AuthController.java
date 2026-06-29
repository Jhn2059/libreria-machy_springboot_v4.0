package com.machy.controller;

import com.machy.dto.request.LoginRequest;
import com.machy.dto.request.PasswordRecoveryRequest;
import com.machy.dto.response.ApiResponse;
import com.machy.dto.response.LoginResponse;
import com.machy.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(ApiResponse.ok(response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/recover")
    public ResponseEntity<ApiResponse<?>> recover(@Valid @RequestBody PasswordRecoveryRequest request) {
        try {
            var result = authService.recoverPassword(request);
            return ResponseEntity.ok(ApiResponse.ok((String) result.get("mensaje"), result));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        return ResponseEntity.ok(ApiResponse.ok("Sesión cerrada correctamente", null));
    }
}
