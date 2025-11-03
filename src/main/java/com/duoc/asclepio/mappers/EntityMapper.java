package com.duoc.asclepio.mappers;

import com.duoc.asclepio.dto.*;
import com.duoc.asclepio.models.Analysis;
import com.duoc.asclepio.models.Lab;

import java.util.stream.Collectors;

public class EntityMapper {

    public static AnalysisDTO toAnalysisDTO(Analysis analysis) {
        return new AnalysisDTO(
                analysis.getId(),
                analysis.getName(),
                analysis.getDescription(),
                analysis.getPrice(),
                analysis.getLab() != null ? analysis.getLab().getId() : null,
                analysis.getLab() != null ? analysis.getLab().getName() : null
        );
    }

    public static LabDTO toLabDTO(Lab lab) {
        return new LabDTO(
                lab.getId(),
                lab.getName(),
                lab.getAddress(),
                lab.getPhone(),
                lab.getAnalyses() != null
                        ? lab.getAnalyses().stream()
                              .map(a -> new AnalysisSummaryDTO(a.getId(), a.getName(), a.getPrice()))
                              .collect(Collectors.toList())
                        : null
        );
    }
}   
