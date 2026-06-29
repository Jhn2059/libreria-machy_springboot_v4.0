package com.machy.controller;

import com.machy.dto.response.ApiResponse;
import com.machy.service.SupplierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(supplierService.findActive()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@RequestBody Map<String, String> body) {
        try {
            String nombre = body.get("nombre");
            if (nombre == null || nombre.isBlank()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Nombre requerido"));
            }
            return ResponseEntity.ok(ApiResponse.ok(
                    supplierService.create(nombre, body.get("ruc"), body.get("contacto"), body.get("telefono"))));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable UUID id) {
        try {
            supplierService.deactivate(id);
            return ResponseEntity.ok(ApiResponse.ok("Proveedor desactivado", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
