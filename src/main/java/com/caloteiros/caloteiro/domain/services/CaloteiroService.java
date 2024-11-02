package com.caloteiros.caloteiro.domain.services;

import com.caloteiros.caloteiro.domain.entities.Caloteiro;
import com.caloteiros.caloteiro.domain.exceptions.CaloteiroNotFoundException;
import com.caloteiros.caloteiro.domain.repositories.CaloteiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaloteiroService {

    @Autowired
    CaloteiroRepository caloteiroRepository;

    public List<Caloteiro> findAll() {
        return caloteiroRepository.findAll();
    }

    public Caloteiro findById(Long caloteiroId) {
        return caloteiroRepository.findById(caloteiroId)
            .orElseThrow(() -> new CaloteiroNotFoundException("Caloteiro não encontrado com o ID: " + caloteiroId));
    }

    public void create(Caloteiro caloteiro) {
        caloteiroRepository.save(caloteiro);
    }

    public void deleteById(Long caloteiroId) {
        caloteiroRepository.deleteById(caloteiroId);
    }

    public void update(Caloteiro updateCaloteiro) {
        Caloteiro caloteiro = caloteiroRepository.findById(updateCaloteiro.getId())
                .orElseThrow(() -> new CaloteiroNotFoundException("Caloteiro não encontrado com o ID: " + updateCaloteiro.getId()));

        updateFields(updateCaloteiro, caloteiro);

        caloteiroRepository.save(caloteiro);
    }

    private void updateFields(Caloteiro updateCaloteiro, Caloteiro caloteiro) {
        caloteiro.setName(updateCaloteiro.getName());
        caloteiro.setEmail(updateCaloteiro.getEmail());
        caloteiro.setDebt(updateCaloteiro.getDebt());
        caloteiro.setDebtDate(updateCaloteiro.getDebtDate());
        caloteiro.setUserId(updateCaloteiro.getUserId());
    }
}
