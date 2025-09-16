package com.caloteiros.user.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserDTO(
        @NotBlank(message = "O nome não pode estar em branco.")
        String name,

        @NotBlank(message = "O email não pode estar em branco.")
        @Email(message = "formato de email inválido.")
        String email,

        String currentPassword,
        String newPassword,
        String confirmPassword
) {
}
