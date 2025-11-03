package com.duoc.asclepio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private List<AnalysisSummaryDTO> analyses;

    public LabDTO(Long id, String name, String address, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }
}
