package com.duoc.asclepio.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultCreateDTO {
    private Long pacientId;
    private Long analysisId;
    private Long labId;

    private String resultValue;
    private String resultDetails;
    private LocalDate resultDate;
}
