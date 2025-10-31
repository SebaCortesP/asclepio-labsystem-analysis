package com.duoc.asclepio.repository;

import com.duoc.asclepio.models.Lab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabRepository extends JpaRepository<Lab, Long> {
    boolean existsByName(String name);
}
