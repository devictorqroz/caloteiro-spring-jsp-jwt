package com.caloteiros.caloteiro.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Dados para cadastrar um novo caloteiro")
public record CreateCaloteiroDTO(

    @Schema(description = "Nome do caloteiro", example = "João Silva")
    @NotBlank(message = "O nome é obrigatório.")
    String name,

    @Schema(description = "Email do caloteiro", example = "joãosilva@email.com")
    @NotBlank(message = "O email é obrigatório.")
    @Email(message = "O email deve ser válido.")
    String email,

    @Schema(description = "Valor da dívida", example = "23.00")
    @NotNull(message = "A dívida é obrigatória.")
    @DecimalMin(value = "0.01", message = "A dívida deve ser maior que zero.")
    BigDecimal debt,

    @Schema(description = "Data da dívida", example = "2025-05-01")
    @NotNull(message = "A data da dívida é obrigatória")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    LocalDate debtDate
) {
}