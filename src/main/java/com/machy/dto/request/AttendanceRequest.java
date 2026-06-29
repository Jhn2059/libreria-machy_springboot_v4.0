package com.machy.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public class AttendanceRequest {
    private UUID usuarioId;

    @NotBlank
    private String tipo;

    public AttendanceRequest() {
    }

    public AttendanceRequest(UUID usuarioId, String tipo) {
        this.usuarioId = usuarioId;
        this.tipo = tipo;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
