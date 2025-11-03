package com.duoc.asclepio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisSummaryDTO {
    private Long id;
    private String name;
    private Double price;
}
