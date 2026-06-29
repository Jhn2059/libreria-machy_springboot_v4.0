package com.machy.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "configuracion")
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String clave;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String valor;

    @Column(length = 50)
    private String tipo;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public Config() {}

    public Config(UUID id, String clave, String valor, String tipo, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.clave = clave;
        this.valor = valor;
        this.tipo = tipo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Config config = (Config) o;
        return Objects.equals(id, config.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Config(id=" + id + ")";
    }

    @PrePersist
    void onCreate() { createdAt = Instant.now(); updatedAt = Instant.now(); }

    @PreUpdate
    void onUpdate() { updatedAt = Instant.now(); }

    public static ConfigBuilder builder() { return new ConfigBuilder(); }

    public static class ConfigBuilder {
        private UUID id;
        private String clave;
        private String valor;
        private String tipo = "text";
        private Instant createdAt;
        private Instant updatedAt;

        public ConfigBuilder id(UUID id) { this.id = id; return this; }
        public ConfigBuilder clave(String clave) { this.clave = clave; return this; }
        public ConfigBuilder valor(String valor) { this.valor = valor; return this; }
        public ConfigBuilder tipo(String tipo) { this.tipo = tipo; return this; }
        public ConfigBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public ConfigBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public Config build() {
            return new Config(id, clave, valor, tipo, createdAt, updatedAt);
        }
    }
}
