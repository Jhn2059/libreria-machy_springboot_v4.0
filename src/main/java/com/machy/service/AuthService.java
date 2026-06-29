package com.machy.service;

import com.machy.dto.request.LoginRequest;
import com.machy.dto.request.PasswordRecoveryRequest;
import com.machy.dto.response.LoginResponse;
import com.machy.entity.LogEntry;
import com.machy.entity.User;
import com.machy.repository.LogRepository;
import com.machy.repository.UserRepository;
import com.machy.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final LogRepository logRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, LogRepository logRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.logRepository = logRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    public LoginResponse login(LoginRequest request) {
        var userOpt = userRepository.findByUsername(request.getUsername().toLowerCase());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        var user = userOpt.get();

        if (!user.getActivo()) {
            throw new RuntimeException("Cuenta desactivada. Contacta al administrador.");
        }

        if (user.getBloqueadoHasta() != null && user.getBloqueadoHasta().isAfter(Instant.now())) {
            long mins = (user.getBloqueadoHasta().getEpochSecond() - Instant.now().getEpochSecond()) / 60;
            throw new RuntimeException("Cuenta bloqueada. Intenta de nuevo en " + mins + " min");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            user.setIntentosFallidos(user.getIntentosFallidos() + 1);
            if (user.getIntentosFallidos() >= 5) {
                user.setBloqueadoHasta(Instant.now().plusSeconds(15 * 60));
            }
            userRepository.save(user);

            logRepository.save(LogEntry.builder()
                    .nivel("warning").modulo("auth")
                    .mensaje("Intento fallido de login: " + request.getUsername())
                    .build());

            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        user.setIntentosFallidos(0);
        user.setBloqueadoHasta(null);
        user.setUltimoAcceso(Instant.now());
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRol());

        logRepository.save(LogEntry.builder()
                .nivel("info").modulo("auth")
                .mensaje("Login exitoso: " + user.getNombreCompleto())
                .usuario(user)
                .build());

        return LoginResponse.builder()
                .token(token)
                .id(user.getId().toString())
                .nombre(user.getNombre())
                .apellidos(user.getApellidos())
                .username(user.getUsername())
                .rol(user.getRol())
                .turno(user.getTurno())
                .av((user.getNombre().charAt(0) + "" + user.getApellidos().charAt(0)).toUpperCase())
                .build();
    }

    public Map<String, Object> recoverPassword(PasswordRecoveryRequest request) {
        var opt = userRepository.findByUsernameOrCorreo(
                request.getUsernameOrEmail(), request.getUsernameOrEmail());

        if (opt.isEmpty()) {
            throw new RuntimeException("No encontramos una cuenta con ese dato.");
        }

        var user = opt.get();

        String newPassword = generarPasswordTemporal();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        logRepository.save(LogEntry.builder()
                .nivel("info").modulo("auth")
                .mensaje("Contraseña restablecida para: " + request.getUsernameOrEmail())
                .usuario(user)
                .contexto("{\"usernameOrEmail\":\"" + request.getUsernameOrEmail() + "\"}")
                .build());

        return Map.of(
                "mensaje", "Tu contraseña ha sido restablecida",
                "username", user.getUsername(),
                "password", newPassword,
                "nombre", user.getNombre(),
                "apellidos", user.getApellidos()
        );
    }

    private String generarPasswordTemporal() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789!@#$";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public void registrarLog(String nivel, String modulo, String mensaje, String usuarioId) {
        User user = null;
        if (usuarioId != null) {
            try {
                var uid = java.util.UUID.fromString(usuarioId);
                user = userRepository.findById(uid).orElse(null);
            } catch (IllegalArgumentException e) {
                // ignore invalid UUID
            }
        }
        logRepository.save(LogEntry.builder()
                .nivel(nivel).modulo(modulo).mensaje(mensaje).usuario(user).build());
    }
}
