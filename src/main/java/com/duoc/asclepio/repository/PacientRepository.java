package com.duoc.asclepio.repository;

import com.duoc.asclepio.models.Pacient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacientRepository extends JpaRepository<Pacient, Long> {
    boolean existsByEmail(String email);
    boolean existsByUserId(Long userId);
}
