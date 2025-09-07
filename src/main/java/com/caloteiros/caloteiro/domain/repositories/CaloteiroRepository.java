package com.caloteiros.caloteiro.domain.repositories;

import com.caloteiros.caloteiro.domain.entities.Caloteiro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CaloteiroRepository extends JpaRepository<Caloteiro, Long> {
    Page<Caloteiro> findByNameContainingIgnoreCaseAndUserId(String name, Long userId, Pageable pageable);
    Optional<Caloteiro> findByIdAndUserId(Long id, Long userId);
    Page<Caloteiro> findByUserId(Long userId, Pageable pageable);
}
