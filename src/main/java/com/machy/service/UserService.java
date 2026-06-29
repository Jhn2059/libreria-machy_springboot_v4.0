package com.machy.service;

import com.machy.dto.request.UserRequest;
import com.machy.entity.User;
import com.machy.repository.AttendanceRepository;
import com.machy.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AttendanceRepository attendanceRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAllByOrderByNombre();
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public User create(UserRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Nombre de usuario ya existe");
        }
        if (userRepository.existsByCorreo(req.getCorreo())) {
            throw new RuntimeException("Correo ya registrado");
        }

        User user = User.builder()
                .nombre(req.getNombre())
                .apellidos(req.getApellidos())
                .dni(req.getDni() != null ? req.getDni() : "")
                .telefono(req.getTelefono() != null ? req.getTelefono() : "")
                .correo(req.getCorreo())
                .username(req.getUsername().toLowerCase())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .rol(req.getRol() != null ? req.getRol() : "vendedor")
                .turno(req.getTurno() != null ? req.getTurno() : "completo")
                .activo(true)
                .build();

        return userRepository.save(user);
    }

    public User update(UUID id, UserRequest req) {
        User user = findById(id);

        if (req.getNombre() != null) user.setNombre(req.getNombre());
        if (req.getApellidos() != null) user.setApellidos(req.getApellidos());
        if (req.getDni() != null) user.setDni(req.getDni());
        if (req.getTelefono() != null) user.setTelefono(req.getTelefono());
        if (req.getCorreo() != null) user.setCorreo(req.getCorreo());
        if (req.getRol() != null) user.setRol(req.getRol());
        if (req.getTurno() != null) user.setTurno(req.getTurno());
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        }

        return userRepository.save(user);
    }

    public User toggleStatus(UUID id) {
        User user = findById(id);
        user.setActivo(!user.getActivo());
        return userRepository.save(user);
    }

    public Map<String, Object> getDashboardData(UUID currentUserId) {
        long activeUsers = userRepository.findByRolAndActivoTrue("vendedor").size();
        long totalRegistros = attendanceRepository.count();
        long cumplimiento = attendanceRepository.countByCumpleTurnoTrue();
        long tardanzas = attendanceRepository.countByEstadoAsistencia("tardanza");

        return Map.of(
                "empleadosActivos", activeUsers,
                "totalRegistros", totalRegistros,
                "cumplimiento", cumplimiento + "/" + totalRegistros,
                "tardanzas", tardanzas
        );
    }
}
