package com.duoc.asclepio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.asclepio.dto.ApiResponse;
import com.duoc.asclepio.dto.ResultCreateDTO;
import com.duoc.asclepio.dto.ResultDTO;
import com.duoc.asclepio.services.ResultService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/results")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;

    @PostMapping
    public ResponseEntity<ApiResponse<ResultDTO>> create(@RequestBody ResultCreateDTO dto) {

        ResultDTO created = resultService.create(dto);

        return ResponseEntity.ok(
            new ApiResponse<>(true, "Resultado creado correctamente", created)
        );
    }
}
