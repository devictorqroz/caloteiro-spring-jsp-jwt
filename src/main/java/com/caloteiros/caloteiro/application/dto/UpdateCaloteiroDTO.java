package com.caloteiros.caloteiro.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateCaloteiroDTO(
        Long id,

        @NotBlank(message = "O nome é obrigatório.")
        String name,

        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "O email deve ser válido.")
        String email,

        @NotNull(message = "A dívida é obrigatória.")
        @DecimalMin(value = "0.01", message = "A dívida deve ser maior que zero.")
        BigDecimal debt,

        LocalDate debtDate
) {
}
