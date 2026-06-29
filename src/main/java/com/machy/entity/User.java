package com.machy.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(length = 8, unique = true)
    private String dni;

    @Column(length = 20)
    private String telefono;

    @Column(nullable = false, unique = true, length = 200)
    private String correo;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false, columnDefinition = "TEXT")
    private String passwordHash;

    @Column(length = 20)
    private String rol;

    @Column(length = 20)
    private String turno;

    @Column(nullable = false)
    private Boolean activo;

    @Column(name = "intentos_fallidos")
    private Integer intentosFallidos;

    @Column(name = "bloqueado_hasta")
    private Instant bloqueadoHasta;

    @Column(name = "ultimo_acceso")
    private Instant ultimoAcceso;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public User() {}

    public User(UUID id, String nombre, String apellidos, String dni, String telefono, String correo, String username, String passwordHash, String rol, String turno, Boolean activo, Integer intentosFallidos, Instant bloqueadoHasta, Instant ultimoAcceso, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.telefono = telefono;
        this.correo = correo;
        this.username = username;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.turno = turno;
        this.activo = activo;
        this.intentosFallidos = intentosFallidos;
        this.bloqueadoHasta = bloqueadoHasta;
        this.ultimoAcceso = ultimoAcceso;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public Integer getIntentosFallidos() { return intentosFallidos; }
    public void setIntentosFallidos(Integer intentosFallidos) { this.intentosFallidos = intentosFallidos; }
    public Instant getBloqueadoHasta() { return bloqueadoHasta; }
    public void setBloqueadoHasta(Instant bloqueadoHasta) { this.bloqueadoHasta = bloqueadoHasta; }
    public Instant getUltimoAcceso() { return ultimoAcceso; }
    public void setUltimoAcceso(Instant ultimoAcceso) { this.ultimoAcceso = ultimoAcceso; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User(id=" + id + ")";
    }

    @PrePersist
    void onCreate() { createdAt = Instant.now(); updatedAt = Instant.now(); }

    @PreUpdate
    void onUpdate() { updatedAt = Instant.now(); }

    public String getNombreCompleto() {
        return nombre + " " + apellidos;
    }

    public static UserBuilder builder() { return new UserBuilder(); }

    public static class UserBuilder {
        private UUID id;
        private String nombre;
        private String apellidos;
        private String dni = "";
        private String telefono = "";
        private String correo;
        private String username;
        private String passwordHash;
        private String rol = "vendedor";
        private String turno = "completo";
        private Boolean activo = true;
        private Integer intentosFallidos = 0;
        private Instant bloqueadoHasta;
        private Instant ultimoAcceso;
        private Instant createdAt;
        private Instant updatedAt;

        public UserBuilder id(UUID id) { this.id = id; return this; }
        public UserBuilder nombre(String nombre) { this.nombre = nombre; return this; }
        public UserBuilder apellidos(String apellidos) { this.apellidos = apellidos; return this; }
        public UserBuilder dni(String dni) { this.dni = dni; return this; }
        public UserBuilder telefono(String telefono) { this.telefono = telefono; return this; }
        public UserBuilder correo(String correo) { this.correo = correo; return this; }
        public UserBuilder username(String username) { this.username = username; return this; }
        public UserBuilder passwordHash(String passwordHash) { this.passwordHash = passwordHash; return this; }
        public UserBuilder rol(String rol) { this.rol = rol; return this; }
        public UserBuilder turno(String turno) { this.turno = turno; return this; }
        public UserBuilder activo(Boolean activo) { this.activo = activo; return this; }
        public UserBuilder intentosFallidos(Integer intentosFallidos) { this.intentosFallidos = intentosFallidos; return this; }
        public UserBuilder bloqueadoHasta(Instant bloqueadoHasta) { this.bloqueadoHasta = bloqueadoHasta; return this; }
        public UserBuilder ultimoAcceso(Instant ultimoAcceso) { this.ultimoAcceso = ultimoAcceso; return this; }
        public UserBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public UserBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public User build() {
            return new User(id, nombre, apellidos, dni, telefono, correo, username, passwordHash, rol, turno, activo, intentosFallidos, bloqueadoHasta, ultimoAcceso, createdAt, updatedAt);
        }
    }
}
