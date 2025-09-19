package com.caloteiros.user.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordDTO(
        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "Por favor, insira um email válido.")
        String email
) {}
