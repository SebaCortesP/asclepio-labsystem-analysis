package com.duoc.asclepio.mappers;

import com.duoc.asclepio.dto.PacientDTO;
import com.duoc.asclepio.models.Pacient;


public class PacientMapper {

    public static PacientDTO toDto(Pacient entity) {
        if (entity == null) return null;

        PacientDTO dto = new PacientDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setBirthDate(entity.getBirthDate());
        dto.setPhone(entity.getPhone());
        dto.setAddress(entity.getAddress());

        return dto;
    }

    public static Pacient toEntity(PacientDTO dto) {
        if (dto == null) return null;

        Pacient entity = new Pacient();
        entity.setId(dto.getId());
        entity.setUserId(dto.getUserId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setBirthDate(dto.getBirthDate());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());

        return entity;
    }
}