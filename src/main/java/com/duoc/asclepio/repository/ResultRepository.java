package com.duoc.asclepio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.duoc.asclepio.models.Result;

public interface ResultRepository extends JpaRepository<Result, Long>{
    List<Result> findByPacientId(Long pacientId);
}
