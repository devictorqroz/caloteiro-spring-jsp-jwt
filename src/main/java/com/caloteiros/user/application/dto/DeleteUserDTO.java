package com.caloteiros.user.application.dto;

import jakarta.validation.constraints.NotBlank;

public record DeleteUserDTO(

        @NotBlank(message = "O password é obrigatório.")
        String password
) {
}
