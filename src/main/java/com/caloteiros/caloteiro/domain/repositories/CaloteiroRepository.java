package com.caloteiros.caloteiro.domain.repositories;

import com.caloteiros.caloteiro.domain.entities.Caloteiro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CaloteiroRepository extends JpaRepository<Caloteiro, Long> {
    Page<Caloteiro> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Caloteiro> findByUserEmail(String email, Pageable pageable);
    Optional<Caloteiro> findByIdAndUserUsername(Long id, String username);
}
