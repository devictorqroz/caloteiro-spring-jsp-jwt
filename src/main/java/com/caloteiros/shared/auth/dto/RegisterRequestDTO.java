package com.caloteiros.shared.auth.dto;

import com.caloteiros.user.domain.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(
    @NotBlank(message = "Nome é obrigatório")
    String name,
    @Email(message = "Email inválido")
    String email,
    @NotBlank(message = "Senha é obrigatória")
    String password
) {
    public User toUser() {
        return new User(this.name, this.email, this.password);
    }
}
