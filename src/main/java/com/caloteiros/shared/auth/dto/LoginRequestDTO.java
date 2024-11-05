package com.caloteiros.shared.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginRequestDTO (@Email String email, @NotNull String password) {
}