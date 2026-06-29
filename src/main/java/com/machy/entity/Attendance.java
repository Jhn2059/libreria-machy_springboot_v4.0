package com.machy.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "asistencia")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private User usuario;

    @Column(length = 200)
    private String nombre;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "hora_entrada")
    private LocalTime horaEntrada;

    @Column(name = "hora_salida")
    private LocalTime horaSalida;

    @Column(length = 20)
    private String turno;

    @Column(precision = 5, scale = 2)
    private BigDecimal horas;

    @Column(name = "tardanza_min")
    private Integer tardanzaMin;

    @Column(name = "cumple_turno")
    private Boolean cumpleTurno;

    @Column(name = "estado_asistencia", length = 20)
    private String estadoAsistencia;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    public Attendance() {}

    public Attendance(UUID id, User usuario, String nombre, LocalDate fecha, LocalTime horaEntrada, LocalTime horaSalida, String turno, BigDecimal horas, Integer tardanzaMin, Boolean cumpleTurno, String estadoAsistencia, Instant createdAt) {
        this.id = id;
        this.usuario = usuario;
        this.nombre = nombre;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.turno = turno;
        this.horas = horas;
        this.tardanzaMin = tardanzaMin;
        this.cumpleTurno = cumpleTurno;
        this.estadoAsistencia = estadoAsistencia;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public User getUsuario() { return usuario; }
    public void setUsuario(User usuario) { this.usuario = usuario; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public LocalTime getHoraEntrada() { return horaEntrada; }
    public void setHoraEntrada(LocalTime horaEntrada) { this.horaEntrada = horaEntrada; }
    public LocalTime getHoraSalida() { return horaSalida; }
    public void setHoraSalida(LocalTime horaSalida) { this.horaSalida = horaSalida; }
    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }
    public BigDecimal getHoras() { return horas; }
    public void setHoras(BigDecimal horas) { this.horas = horas; }
    public Integer getTardanzaMin() { return tardanzaMin; }
    public void setTardanzaMin(Integer tardanzaMin) { this.tardanzaMin = tardanzaMin; }
    public Boolean getCumpleTurno() { return cumpleTurno; }
    public void setCumpleTurno(Boolean cumpleTurno) { this.cumpleTurno = cumpleTurno; }
    public String getEstadoAsistencia() { return estadoAsistencia; }
    public void setEstadoAsistencia(String estadoAsistencia) { this.estadoAsistencia = estadoAsistencia; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendance that = (Attendance) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Attendance(id=" + id + ")";
    }

    @PrePersist
    void onCreate() { createdAt = Instant.now(); }

    public static AttendanceBuilder builder() { return new AttendanceBuilder(); }

    public static class AttendanceBuilder {
        private UUID id;
        private User usuario;
        private String nombre = "";
        private LocalDate fecha;
        private LocalTime horaEntrada;
        private LocalTime horaSalida;
        private String turno = "completo";
        private BigDecimal horas;
        private Integer tardanzaMin;
        private Boolean cumpleTurno = true;
        private String estadoAsistencia = "puntual";
        private Instant createdAt;

        public AttendanceBuilder id(UUID id) { this.id = id; return this; }
        public AttendanceBuilder usuario(User usuario) { this.usuario = usuario; return this; }
        public AttendanceBuilder nombre(String nombre) { this.nombre = nombre; return this; }
        public AttendanceBuilder fecha(LocalDate fecha) { this.fecha = fecha; return this; }
        public AttendanceBuilder horaEntrada(LocalTime horaEntrada) { this.horaEntrada = horaEntrada; return this; }
        public AttendanceBuilder horaSalida(LocalTime horaSalida) { this.horaSalida = horaSalida; return this; }
        public AttendanceBuilder turno(String turno) { this.turno = turno; return this; }
        public AttendanceBuilder horas(BigDecimal horas) { this.horas = horas; return this; }
        public AttendanceBuilder tardanzaMin(Integer tardanzaMin) { this.tardanzaMin = tardanzaMin; return this; }
        public AttendanceBuilder cumpleTurno(Boolean cumpleTurno) { this.cumpleTurno = cumpleTurno; return this; }
        public AttendanceBuilder estadoAsistencia(String estadoAsistencia) { this.estadoAsistencia = estadoAsistencia; return this; }
        public AttendanceBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }

        public Attendance build() {
            return new Attendance(id, usuario, nombre, fecha, horaEntrada, horaSalida, turno, horas, tardanzaMin, cumpleTurno, estadoAsistencia, createdAt);
        }
    }
}
