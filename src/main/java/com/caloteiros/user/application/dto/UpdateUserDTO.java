package com.caloteiros.user.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserDTO(
        @NotBlank(message = "O nome é obrigatório.")
        String name,

        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "O email deve ser válido.")
        String email,

        @NotBlank(message = "O password é obrigatório.")
        String newPassword,

        @NotBlank(message = "O confirmação de password é obrigatório.")
        String confirmPassword
) {
}
