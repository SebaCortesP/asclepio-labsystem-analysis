package com.duoc.asclepio.services;

import org.springframework.stereotype.Service;

import com.duoc.asclepio.dto.ResultCreateDTO;
import com.duoc.asclepio.dto.ResultDTO;
import com.duoc.asclepio.models.Analysis;
import com.duoc.asclepio.models.Lab;
import com.duoc.asclepio.models.Pacient;
import com.duoc.asclepio.models.Result;
import com.duoc.asclepio.repository.AnalysisRepository;
import com.duoc.asclepio.repository.LabRepository;
import com.duoc.asclepio.repository.PacientRepository;
import com.duoc.asclepio.repository.ResultRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResultService {

        private final ResultRepository resultRepository;
        private final AnalysisRepository analysisRepository;
        private final LabRepository labRepository;
        public ResultDTO create(ResultCreateDTO dto) {
                Analysis analysis = analysisRepository.findById(dto.getAnalysisId())
                        .orElseThrow(() -> new RuntimeException("Análisis no encontrado"));

                Lab lab = labRepository.findById(dto.getLabId())
                        .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado"));

                Result r = new Result();
                r.setUserId(dto.getPacientId());  // <-- ESTE ES EL ID EXTERNO
                r.setAnalysis(analysis);
                r.setLab(lab);
                r.setResultValue(dto.getResultValue());
                r.setResultDetails(dto.getResultDetails());
                r.setResultDate(dto.getResultDate());

                resultRepository.save(r);

                return ResultDTO.fromEntity(r);
        }

}
