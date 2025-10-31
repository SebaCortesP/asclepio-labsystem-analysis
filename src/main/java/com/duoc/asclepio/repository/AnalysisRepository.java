package com.duoc.asclepio.repository;

import com.duoc.asclepio.models.Analysis;
import com.duoc.asclepio.models.Lab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Long> {
    List<Analysis> findByLab(Lab lab);
    boolean existsByNameAndLab(String name, Lab lab);
}
