package com.caloteiros.caloteiro.domain.repositories;

import com.caloteiros.caloteiro.domain.entities.Caloteiro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaloteiroRepository extends JpaRepository<Caloteiro, Long> {
}
