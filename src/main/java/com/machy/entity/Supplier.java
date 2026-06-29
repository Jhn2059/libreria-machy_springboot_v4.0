package com.machy.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "proveedores")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(length = 11)
    private String ruc;

    @Column(length = 100)
    private String contacto;

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String email;

    @Column(columnDefinition = "TEXT DEFAULT ''")
    private String direccion;

    @Column(nullable = false)
    private Boolean activo;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public Supplier() {}

    public Supplier(UUID id, String nombre, String ruc, String contacto, String telefono, String email, String direccion, Boolean activo, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.nombre = nombre;
        this.ruc = ruc;
        this.contacto = contacto;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.activo = activo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }
    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Supplier supplier = (Supplier) o;
        return Objects.equals(id, supplier.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Supplier(id=" + id + ")";
    }

    @PrePersist
    void onCreate() { createdAt = Instant.now(); updatedAt = Instant.now(); }

    @PreUpdate
    void onUpdate() { updatedAt = Instant.now(); }

    public static SupplierBuilder builder() { return new SupplierBuilder(); }

    public static class SupplierBuilder {
        private UUID id;
        private String nombre;
        private String ruc = "";
        private String contacto = "";
        private String telefono = "";
        private String email = "";
        private String direccion = "";
        private Boolean activo = true;
        private Instant createdAt;
        private Instant updatedAt;

        public SupplierBuilder id(UUID id) { this.id = id; return this; }
        public SupplierBuilder nombre(String nombre) { this.nombre = nombre; return this; }
        public SupplierBuilder ruc(String ruc) { this.ruc = ruc; return this; }
        public SupplierBuilder contacto(String contacto) { this.contacto = contacto; return this; }
        public SupplierBuilder telefono(String telefono) { this.telefono = telefono; return this; }
        public SupplierBuilder email(String email) { this.email = email; return this; }
        public SupplierBuilder direccion(String direccion) { this.direccion = direccion; return this; }
        public SupplierBuilder activo(Boolean activo) { this.activo = activo; return this; }
        public SupplierBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public SupplierBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public Supplier build() {
            return new Supplier(id, nombre, ruc, contacto, telefono, email, direccion, activo, createdAt, updatedAt);
        }
    }
}
