package com.machy.controller;

import com.machy.dto.request.ProductRequest;
import com.machy.dto.response.ApiResponse;
import com.machy.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll(@RequestParam(required = false) String q,
                                                  @RequestParam(required = false) String categoria,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "99999") int size) {
        if (q != null && !q.isBlank()) {
            return ResponseEntity.ok(ApiResponse.ok(productService.buscar(q, categoria)));
        }
        if (page > 0 || size < 99999) {
            return ResponseEntity.ok(ApiResponse.ok(
                    productService.findAll(PageRequest.of(page, size, Sort.by("nombre")))));
        }
        return ResponseEntity.ok(ApiResponse.ok(productService.findAll()));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<?>> getActive() {
        return ResponseEntity.ok(ApiResponse.ok(productService.findActive()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(productService.findById(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/by-code/{codigo}")
    public ResponseEntity<?> getByCode(@PathVariable String codigo) {
        var opt = productService.findAll().stream()
                .filter(p -> p.getCodigo().equals(codigo)).findFirst();
        if (opt.isPresent()) {
            return ResponseEntity.ok(ApiResponse.ok(opt.get()));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("Producto no encontrado"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@Valid @RequestBody ProductRequest request,
                                                  Authentication auth) {
        try {
            var principal = (com.machy.security.JwtAuthenticationFilter.UserPrincipal) auth.getPrincipal();
            if (!"admin".equals(principal.rol())) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Solo administradores"));
            }
            return ResponseEntity.ok(ApiResponse.ok(productService.create(request)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(@PathVariable UUID id,
                                                  @Valid @RequestBody ProductRequest request,
                                                  Authentication auth) {
        try {
            var principal = (com.machy.security.JwtAuthenticationFilter.UserPrincipal) auth.getPrincipal();
            if (!"admin".equals(principal.rol())) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Solo administradores"));
            }
            return ResponseEntity.ok(ApiResponse.ok(productService.update(id, request)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<?>> toggle(@PathVariable UUID id, Authentication auth) {
        try {
            var principal = (com.machy.security.JwtAuthenticationFilter.UserPrincipal) auth.getPrincipal();
            if (!"admin".equals(principal.rol())) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Solo administradores"));
            }
            return ResponseEntity.ok(ApiResponse.ok(productService.toggleEstado(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
