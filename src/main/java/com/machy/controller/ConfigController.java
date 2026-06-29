package com.machy.controller;

import com.machy.dto.response.ApiResponse;
import com.machy.service.ConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(configService.findAllAsMap()));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<?>> update(@RequestBody Map<String, String> configMap) {
        try {
            configService.saveFromMap(configMap);
            return ResponseEntity.ok(ApiResponse.ok("Configuración guardada", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
