package com.duoc.asclepio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisRequestDTO {
    private String name;
    private String description;
    private Double price;
    private Long labId;
}
