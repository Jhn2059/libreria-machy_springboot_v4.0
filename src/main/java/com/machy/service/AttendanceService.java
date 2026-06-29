package com.machy.service;

import com.machy.entity.Attendance;
import com.machy.entity.User;
import com.machy.repository.AttendanceRepository;
import com.machy.repository.LogRepository;
import com.machy.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final LogRepository logRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, UserRepository userRepository, LogRepository logRepository) {
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
        this.logRepository = logRepository;
    }

    public List<Attendance> findAll() {
        return attendanceRepository.findAllByOrderByFechaDesc();
    }

    public Map<String, Object> getStatusHoy(UUID usuarioId) {
        LocalDate hoy = LocalDate.now();
        var opt = attendanceRepository.findByUsuarioIdAndFecha(usuarioId, hoy);
        return opt.<Map<String, Object>>map(reg -> Map.of(
                "registrado", true,
                "horaEntrada", reg.getHoraEntrada() != null ? reg.getHoraEntrada().toString() : null,
                "horaSalida", reg.getHoraSalida() != null ? reg.getHoraSalida().toString() : null,
                "turno", reg.getTurno(),
                "tardanzaMin", reg.getTardanzaMin(),
                "estado", reg.getEstadoAsistencia()
        )).orElseGet(() -> Map.of("registrado", false));
    }

    @Transactional
    public Attendance marcarEntrada(UUID usuarioId) {
        User user = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        LocalDate hoy = LocalDate.now();
        if (attendanceRepository.findByUsuarioIdAndFecha(usuarioId, hoy).isPresent()) {
            throw new RuntimeException("Ya tienes un registro de entrada hoy");
        }

        LocalTime ahora = LocalTime.now();
        String turno = user.getTurno() != null ? user.getTurno() : "completo";

        int tardanzaMin = calcularTardanza(ahora, turno);

        Attendance attendance = Attendance.builder()
                .usuario(user)
                .nombre(user.getNombreCompleto())
                .fecha(hoy)
                .horaEntrada(ahora)
                .turno(turno)
                .horas(BigDecimal.ZERO)
                .tardanzaMin(tardanzaMin)
                .cumpleTurno(false)
                .estadoAsistencia(tardanzaMin > 0 ? "tardanza" : "puntual")
                .build();

        logRepository.save(com.machy.entity.LogEntry.builder()
                .nivel("info").modulo("asistencia")
                .mensaje("Entrada: " + user.getNombreCompleto())
                .usuario(user)
                .build());

        return attendanceRepository.save(attendance);
    }

    @Transactional
    public Attendance marcarSalida(UUID usuarioId) {
        LocalDate hoy = LocalDate.now();
        Attendance reg = attendanceRepository.findByUsuarioIdAndFecha(usuarioId, hoy)
                .orElseThrow(() -> new RuntimeException("No hay registro de entrada hoy"));

        if (reg.getHoraSalida() != null) {
            throw new RuntimeException("Ya registraste salida hoy");
        }

        LocalTime ahora = LocalTime.now();
        reg.setHoraSalida(ahora);

        long minsEntrada = reg.getHoraEntrada().getHour() * 60L + reg.getHoraEntrada().getMinute();
        long minsSalida = ahora.getHour() * 60L + ahora.getMinute();
        BigDecimal horasTrab = BigDecimal.valueOf((minsSalida - minsEntrada) / 60.0)
                .setScale(2, RoundingMode.HALF_UP);

        reg.setHoras(horasTrab);
        reg.setCumpleTurno(horasTrab.compareTo(new BigDecimal("5")) >= 0);

        logRepository.save(com.machy.entity.LogEntry.builder()
                .nivel("info").modulo("asistencia")
                .mensaje("Salida: " + reg.getNombre())
                .usuario(reg.getUsuario())
                .build());

        return attendanceRepository.save(reg);
    }

    @Transactional
    public Attendance registrarAdmin(UUID usuarioId, String tipo) {
        User user = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        var opt = attendanceRepository.findByUsuarioIdAndFecha(usuarioId, hoy);

        if ("entrada".equals(tipo)) {
            if (opt.isPresent() && opt.get().getHoraEntrada() != null) {
                throw new RuntimeException(user.getNombreCompleto() + " ya tiene entrada hoy");
            }
            Attendance reg = opt.orElse(Attendance.builder()
                    .usuario(user)
                    .nombre(user.getNombreCompleto())
                    .fecha(hoy)
                    .turno(user.getTurno() != null ? user.getTurno() : "completo")
                    .estadoAsistencia("puntual")
                    .build());
            reg.setHoraEntrada(ahora);
            return attendanceRepository.save(reg);
        } else {
            Attendance reg = opt.orElseThrow(() ->
                    new RuntimeException(user.getNombreCompleto() + " no tiene entrada hoy"));
            if (reg.getHoraSalida() != null) {
                throw new RuntimeException(user.getNombreCompleto() + " ya tiene salida hoy");
            }
            reg.setHoraSalida(ahora);
            long minsEntrada = reg.getHoraEntrada().getHour() * 60L + reg.getHoraEntrada().getMinute();
            long minsSalida = ahora.getHour() * 60L + ahora.getMinute();
            BigDecimal horasTrab = BigDecimal.valueOf((minsSalida - minsEntrada) / 60.0)
                    .setScale(2, RoundingMode.HALF_UP);
            reg.setHoras(horasTrab);
            reg.setCumpleTurno(horasTrab.compareTo(new BigDecimal("5")) >= 0);
            return attendanceRepository.save(reg);
        }
    }

    public List<Map<String, Object>> getInformeSemanal() {
        var vendedores = userRepository.findByRolAndActivoTrue("vendedor");
        return vendedores.stream().map(u -> {
            var registros = attendanceRepository.findByUsuarioIdOrderByFechaDesc(u.getId());
            long dias = registros.size();
            double hrs = registros.stream()
                    .mapToDouble(a -> a.getHoras() != null ? a.getHoras().doubleValue() : 0)
                    .sum();
            long tardanzas = registros.stream()
                    .filter(a -> "tardanza".equals(a.getEstadoAsistencia())).count();
            long cumplen = registros.stream()
                    .filter(a -> Boolean.TRUE.equals(a.getCumpleTurno())).count();
            int pct = dias > 0 ? (int) (cumplen * 100 / dias) : 0;

            java.util.Map<String, Object> result = new java.util.LinkedHashMap<>();
            result.put("nombre", u.getNombreCompleto());
            result.put("turno", u.getTurno());
            result.put("dias", dias);
            result.put("horas", String.format("%.1f", hrs));
            result.put("tardanzas", tardanzas);
            result.put("cumplimiento", pct + "%");
            result.put("estado", pct >= 80 ? "Cumple" : pct >= 50 ? "Parcial" : "No cumple");
            return result;
        }).toList();
    }

    private int calcularTardanza(LocalTime horaActual, String turno) {
        var config = Map.of(
                "manana", LocalTime.of(8, 0),
                "tarde", LocalTime.of(14, 0),
                "completo", LocalTime.of(8, 0)
        );
        LocalTime inicio = config.getOrDefault(turno, LocalTime.of(8, 0));
        long mins = horaActual.getHour() * 60L + horaActual.getMinute();
        long inicioMins = inicio.getHour() * 60L + inicio.getMinute();
        return Math.max(0, (int) (mins - inicioMins - 15));
    }
}
