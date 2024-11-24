package com.caloteiros.caloteiro.application.mapper;

import com.caloteiros.caloteiro.application.dto.CaloteiroPageDTO;
import com.caloteiros.caloteiro.application.dto.CreateCaloteiroDTO;
import com.caloteiros.caloteiro.application.dto.CaloteiroDTO;
import com.caloteiros.caloteiro.application.dto.UpdateCaloteiroDTO;
import com.caloteiros.caloteiro.domain.entities.Caloteiro;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CaloteiroMapper {

    public CaloteiroDTO toCaloteiroDTO(Caloteiro caloteiro) {
        return new CaloteiroDTO(
                caloteiro.getId(),
                caloteiro.getName(),
                caloteiro.getEmail(),
                caloteiro.getDebt(),
                caloteiro.getDebtDate()
        );
    }

    public Caloteiro fromCreateDTOToEntity(CreateCaloteiroDTO dto) {
        return new Caloteiro(
                null,
                dto.name(),
                dto.email(),
                dto.debt(),
                dto.debtDate(),
                null
        );
    }

    public Caloteiro fromUpdateDTOToEntity(UpdateCaloteiroDTO dto, Caloteiro caloteiro) {
        caloteiro.setId(dto.id());
        caloteiro.setName(dto.name());
        caloteiro.setEmail(dto.email());
        caloteiro.setDebt(dto.debt());
        caloteiro.setDebtDate(dto.debtDate());
        return caloteiro;
    }

     public CaloteiroPageDTO getCaloteiroPageDTO(List<CaloteiroDTO> caloteiros, Page<Caloteiro> page) {
         return new CaloteiroPageDTO(
                 caloteiros,
                 page.getNumber(),
                 page.getSize(),
                 page.getTotalElements(),
                 page.getTotalPages(),
                 page.hasPrevious(),
                 page.hasNext(),
                 page.isFirst(),
                 page.isLast()
         );
     }
}
