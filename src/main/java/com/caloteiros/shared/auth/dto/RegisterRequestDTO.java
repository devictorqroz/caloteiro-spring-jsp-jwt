package com.caloteiros.shared.auth.dto;

import com.caloteiros.user.domain.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
    @NotBlank(message = "Nome é obrigatório")
    String name,

    @Email(message = "Email inválido")
    String email,

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    String password
) {
    public User toUser() {
        return new User(null, this.name, this.email, this.password);
    }
}
