package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

public class ResetPasswordRequest {

    @NotBlank(message = "Email is required")
    private String email;

    public ResetPasswordRequest() {
    }

    public String getEmail() {
        return email;
    }

    public ResetPasswordRequest setEmail(String email) {
        this.email = email;
        return this;
    }
}
