package com.duoc.asclepio.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.asclepio.dto.ApiResponse;
import com.duoc.asclepio.dto.ResultCreateDTO;
import com.duoc.asclepio.dto.ResultDTO;
import com.duoc.asclepio.models.Result;
import com.duoc.asclepio.repository.ResultRepository;
import com.duoc.asclepio.security.AllowedRoles;
import com.duoc.asclepio.services.ResultService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/results")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;
    private final ResultRepository resultRepository;

    @PostMapping
    @AllowedRoles({"ADMIN","PACIENTE"})
    public ResponseEntity<ApiResponse<ResultDTO>> create(@RequestBody ResultCreateDTO dto) {

        ResultDTO created = resultService.create(dto);

        return ResponseEntity.ok(
            new ApiResponse<>(true, "Resultado creado correctamente", created)
        );
    }

    @GetMapping("/by-user/{userId}")
    @AllowedRoles({"ADMIN","PACIENTE"})
    public ResponseEntity<ApiResponse<List<ResultDTO>>> getResultsByUser(@PathVariable Long userId) {

        List<Result> results = resultRepository.findByUserId(userId);
        List<ResultDTO> dtos = results.stream()
                .map(ResultDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(
            ApiResponse.success("Resultados obtenidos correctamente", dtos)
        );
    }


}
