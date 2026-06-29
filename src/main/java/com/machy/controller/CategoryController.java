package com.machy.controller;

import com.machy.dto.response.ApiResponse;
import com.machy.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(categoryService.findActive()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@RequestBody Map<String, String> body) {
        try {
            String nombre = body.get("nombre");
            if (nombre == null || nombre.isBlank()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Nombre requerido"));
            }
            return ResponseEntity.ok(ApiResponse.ok(categoryService.create(nombre)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable UUID id) {
        try {
            categoryService.deactivate(id);
            return ResponseEntity.ok(ApiResponse.ok("Categoría desactivada", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
