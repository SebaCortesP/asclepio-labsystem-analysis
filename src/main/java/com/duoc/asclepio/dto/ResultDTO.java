package com.duoc.asclepio.dto;

import java.time.LocalDate;

import com.duoc.asclepio.models.Result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ResultDTO {
    private Long id;
    private String analysisName;
    private String labName;
    private String resultValue;
    private String resultDetails;
    private LocalDate resultDate;

    public static ResultDTO fromEntity(Result r) {
        return new ResultDTO(
            r.getId(),
            r.getAnalysis().getName(),
            r.getLab().getName(),
            r.getResultValue(),
            r.getResultDetails(),
            r.getResultDate()
        );
    }
}
