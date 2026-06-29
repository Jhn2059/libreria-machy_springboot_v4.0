package com.machy.dto.request;

import jakarta.validation.constraints.NotBlank;

public class PasswordRecoveryRequest {
    @NotBlank
    private String usernameOrEmail;

    public PasswordRecoveryRequest() {
    }

    public PasswordRecoveryRequest(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }
}
