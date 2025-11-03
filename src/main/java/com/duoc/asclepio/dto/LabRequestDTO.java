package com.duoc.asclepio.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabRequestDTO {
    private String name;
    private String address;
    private String phone;
}
