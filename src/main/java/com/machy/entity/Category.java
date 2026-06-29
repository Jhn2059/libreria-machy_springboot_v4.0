package com.machy.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "categorias")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT DEFAULT ''")
    private String descripcion;

    @Column(nullable = false)
    private Boolean activo;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public Category() {}

    public Category(UUID id, String nombre, String descripcion, Boolean activo, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
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
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Category(id=" + id + ")";
    }

    @PrePersist
    void onCreate() { createdAt = Instant.now(); updatedAt = Instant.now(); }

    @PreUpdate
    void onUpdate() { updatedAt = Instant.now(); }

    public static CategoryBuilder builder() { return new CategoryBuilder(); }

    public static class CategoryBuilder {
        private UUID id;
        private String nombre;
        private String descripcion = "";
        private Boolean activo = true;
        private Instant createdAt;
        private Instant updatedAt;

        public CategoryBuilder id(UUID id) { this.id = id; return this; }
        public CategoryBuilder nombre(String nombre) { this.nombre = nombre; return this; }
        public CategoryBuilder descripcion(String descripcion) { this.descripcion = descripcion; return this; }
        public CategoryBuilder activo(Boolean activo) { this.activo = activo; return this; }
        public CategoryBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public CategoryBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public Category build() {
            return new Category(id, nombre, descripcion, activo, createdAt, updatedAt);
        }
    }
}
