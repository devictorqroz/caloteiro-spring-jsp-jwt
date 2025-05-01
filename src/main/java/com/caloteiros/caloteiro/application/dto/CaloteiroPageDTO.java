package com.caloteiros.caloteiro.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Página de resultados com caloteiros")
public record CaloteiroPageDTO(

        @Schema(description = "Lista de caloteiros na página atual")
        List<CaloteiroDTO> caloteiros,

        @Schema(description = "Número da página atual", example = "0")
        int pageNumber,

        @Schema(description = "Tamanho da página", example = "10")
        int pageSize,

        @Schema(description = "Total de elementos encontrados", example = "50")
        long totalElements,

        @Schema(description = "Total de páginas disponíveis", example = "5")
        int totalPages,

        @Schema(description = "Indica se há página anterior")
        boolean hasPrevious,

        @Schema(description = "Indica se há próxima página")
        boolean hasNext,

        @Schema(description = "É a primeira página?")
        boolean isFirst,

        @Schema(description = "É a última página?")
        boolean isLast
) {
}
