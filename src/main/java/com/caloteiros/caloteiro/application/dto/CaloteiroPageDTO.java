package com.caloteiros.caloteiro.application.dto;

import java.util.List;

public record CaloteiroPageDTO(
        List<CaloteiroDTO> caloteiros,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean hasPrevious,
        boolean hasNext,
        boolean isFirst,
        boolean isLast
) {
}
