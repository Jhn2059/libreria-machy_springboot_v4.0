package com.machy.controller;

import com.machy.dto.request.UserRequest;
import com.machy.dto.response.ApiResponse;
import com.machy.entity.User;
import com.machy.security.JwtAuthenticationFilter;
import com.machy.service.AttendanceService;
import com.machy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AttendanceService attendanceService;

    public UserController(UserService userService, AttendanceService attendanceService) {
        this.userService = userService;
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "99999") int size) {
        if (page > 0 || size < 99999) {
            return ResponseEntity.ok(ApiResponse.ok(
                    userService.findAll(PageRequest.of(page, size, Sort.by("nombre")))));
        }
        return ResponseEntity.ok(ApiResponse.ok(userService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(userService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@Valid @RequestBody UserRequest request) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(userService.create(request)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(@PathVariable UUID id, @RequestBody UserRequest request) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(userService.update(id, request)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<?>> toggle(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(userService.toggleStatus(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<?>> getDashboard(Authentication auth) {
        var principal = (JwtAuthenticationFilter.UserPrincipal) auth.getPrincipal();
        return ResponseEntity.ok(ApiResponse.ok(userService.getDashboardData(principal.id())));
    }

    @GetMapping("/attendance/status")
    public ResponseEntity<ApiResponse<?>> getAttendanceStatus(Authentication auth) {
        var principal = (JwtAuthenticationFilter.UserPrincipal) auth.getPrincipal();
        return ResponseEntity.ok(ApiResponse.ok(attendanceService.getStatusHoy(principal.id())));
    }

    @PostMapping("/attendance/check-in")
    public ResponseEntity<ApiResponse<?>> checkIn(Authentication auth) {
        try {
            var principal = (JwtAuthenticationFilter.UserPrincipal) auth.getPrincipal();
            return ResponseEntity.ok(ApiResponse.ok(attendanceService.marcarEntrada(principal.id())));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/attendance/check-out")
    public ResponseEntity<ApiResponse<?>> checkOut(Authentication auth) {
        try {
            var principal = (JwtAuthenticationFilter.UserPrincipal) auth.getPrincipal();
            return ResponseEntity.ok(ApiResponse.ok(attendanceService.marcarSalida(principal.id())));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/attendance/admin")
    public ResponseEntity<ApiResponse<?>> adminAttendance(@RequestBody Map<String, String> body, Authentication auth) {
        try {
            var principal = (JwtAuthenticationFilter.UserPrincipal) auth.getPrincipal();
            if (!"admin".equals(principal.rol())) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Solo administradores"));
            }
            UUID uid = UUID.fromString(body.get("usuarioId"));
            String tipo = body.get("tipo");
            return ResponseEntity.ok(ApiResponse.ok(attendanceService.registrarAdmin(uid, tipo)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
