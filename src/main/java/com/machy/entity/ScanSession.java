package com.machy.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "scan_sessions")
public class ScanSession {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Column(columnDefinition = "TEXT")
    private String code;

    @Column(name = "usuario_id", columnDefinition = "TEXT")
    private String usuarioId;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Boolean used;

    public ScanSession() {}

    public ScanSession(UUID id, String sessionId, String code, String usuarioId, Instant createdAt, Boolean used) {
        this.id = id;
        this.sessionId = sessionId;
        this.code = code;
        this.usuarioId = usuarioId;
        this.createdAt = createdAt;
        this.used = used;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Boolean getUsed() { return used; }
    public void setUsed(Boolean used) { this.used = used; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScanSession that = (ScanSession) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ScanSession(id=" + id + ")";
    }

    @PrePersist
    void onCreate() { createdAt = Instant.now(); }

    public static ScanSessionBuilder builder() { return new ScanSessionBuilder(); }

    public static class ScanSessionBuilder {
        private UUID id;
        private String sessionId;
        private String code;
        private String usuarioId;
        private Instant createdAt;
        private Boolean used = false;

        public ScanSessionBuilder id(UUID id) { this.id = id; return this; }
        public ScanSessionBuilder sessionId(String sessionId) { this.sessionId = sessionId; return this; }
        public ScanSessionBuilder code(String code) { this.code = code; return this; }
        public ScanSessionBuilder usuarioId(String usuarioId) { this.usuarioId = usuarioId; return this; }
        public ScanSessionBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public ScanSessionBuilder used(Boolean used) { this.used = used; return this; }

        public ScanSession build() {
            return new ScanSession(id, sessionId, code, usuarioId, createdAt, used);
        }
    }
}
