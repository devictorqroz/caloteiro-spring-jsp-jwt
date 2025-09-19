package com.caloteiros.user.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordDTO(
        @NotBlank
        String token,

        @NotBlank(message = "A nova senha é obrigatória.")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        String password,

        @NotBlank(message = "A confirmação de senha é obrigatória")
        String confirmPassword
) {}
