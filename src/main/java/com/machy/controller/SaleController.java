package com.machy.controller;

import com.machy.dto.request.SaleRequest;
import com.machy.dto.response.ApiResponse;
import com.machy.entity.User;
import com.machy.repository.UserRepository;
import com.machy.security.JwtAuthenticationFilter;
import com.machy.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;
    private final UserRepository userRepository;

    public SaleController(SaleService saleService, UserRepository userRepository) {
        this.saleService = saleService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll(Authentication auth) {
        var principal = (JwtAuthenticationFilter.UserPrincipal) auth.getPrincipal();
        if ("admin".equals(principal.rol())) {
            return ResponseEntity.ok(ApiResponse.ok(saleService.findAll()));
        }
        return ResponseEntity.ok(ApiResponse.ok(saleService.findByVendedor(principal.id())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(saleService.findById(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@Valid @RequestBody SaleRequest request,
                                                  Authentication auth) {
        try {
            var principal = (JwtAuthenticationFilter.UserPrincipal) auth.getPrincipal();
            User vendedor = userRepository.findById(principal.id())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            return ResponseEntity.ok(ApiResponse.ok(saleService.create(request, vendedor)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<?>> cancel(@PathVariable UUID id,
                                                  @RequestBody Map<String, String> body,
                                                  Authentication auth) {
        try {
            var principal = (JwtAuthenticationFilter.UserPrincipal) auth.getPrincipal();
            User admin = userRepository.findById(principal.id())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            String motivo = body.getOrDefault("motivo", "Sin motivo");
            return ResponseEntity.ok(ApiResponse.ok(saleService.anular(id, motivo, admin)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
