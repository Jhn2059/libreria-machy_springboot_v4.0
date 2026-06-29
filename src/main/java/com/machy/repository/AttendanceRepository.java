package com.machy.repository;

import com.machy.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    List<Attendance> findAllByOrderByFechaDesc();
    List<Attendance> findByUsuarioIdOrderByFechaDesc(UUID usuarioId);
    Optional<Attendance> findByUsuarioIdAndFecha(UUID usuarioId, LocalDate fecha);
    long countByEstadoAsistencia(String estado);
    long countByCumpleTurnoTrue();
}
