package com.duoc.asclepio.controller;

import com.duoc.asclepio.dto.*;
import com.duoc.asclepio.mappers.EntityMapper;
import com.duoc.asclepio.models.Analysis;
import com.duoc.asclepio.models.Lab;
import com.duoc.asclepio.repository.AnalysisRepository;
import com.duoc.asclepio.repository.LabRepository;
import com.duoc.asclepio.security.AllowedRoles;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/analyses")
public class AnalysisController {

    private final AnalysisRepository analysisRepository;
    private final LabRepository labRepository;

    public AnalysisController(AnalysisRepository analysisRepository, LabRepository labRepository) {
        this.analysisRepository = analysisRepository;
        this.labRepository = labRepository;
    }

    // Crear análisis
    @PostMapping
    public ResponseEntity<ApiResponse<AnalysisDTO>> createAnalysis(@RequestBody AnalysisRequestDTO request) {
        Optional<Lab> labOpt = labRepository.findById(request.getLabId());
        if (labOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Laboratorio no encontrado", null));
        }

        Lab lab = labOpt.get();

        if (analysisRepository.existsByNameAndLab(request.getName(), lab)) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "El análisis ya existe en este laboratorio", null));
        }

        Analysis analysis = new Analysis();
        analysis.setName(request.getName());
        analysis.setDescription(request.getDescription());
        analysis.setPrice(request.getPrice());
        analysis.setLab(lab);

        Analysis saved = analysisRepository.save(analysis);

        return ResponseEntity.ok(new ApiResponse<>(true, "Análisis creado correctamente", EntityMapper.toAnalysisDTO(saved)));
    }

    // Obtener todos los análisis
    @GetMapping
    public ResponseEntity<ApiResponse<List<AnalysisDTO>>> getAllAnalyses() {
        List<AnalysisDTO> analyses = analysisRepository.findAll()
                .stream()
                .map(EntityMapper::toAnalysisDTO)
                .toList();

        return ResponseEntity.ok(new ApiResponse<>(true, "Listado de análisis obtenido", analyses));
    }

    // Obtener análisis por ID
    @GetMapping("/{id}")
    @AllowedRoles({"ADMIN", "PACIENTE"})
    public ResponseEntity<ApiResponse<AnalysisDTO>> getAnalysisById(@PathVariable Long id) {
        Optional<Analysis> analysisOpt = analysisRepository.findById(id);
        if (analysisOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Análisis no encontrado", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "Análisis obtenido", EntityMapper.toAnalysisDTO(analysisOpt.get())));
    }

    // Obtener análisis por laboratorio
    @GetMapping("/by-lab/{labId}")
    @AllowedRoles({"ADMIN", "PACIENTE"})
    public ResponseEntity<ApiResponse<List<AnalysisDTO>>> getAnalysesByLab(@PathVariable Long labId) {
        Optional<Lab> labOpt = labRepository.findById(labId);
        if (labOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Laboratorio no encontrado", null));
        }

        List<AnalysisDTO> analyses = analysisRepository.findByLab(labOpt.get())
                .stream()
                .map(EntityMapper::toAnalysisDTO)
                .toList();

        return ResponseEntity.ok(new ApiResponse<>(true, "Análisis del laboratorio obtenidos", analyses));
    }

    // Actualizar análisis
    @PutMapping("/{id}")
    @AllowedRoles({"ADMIN"})
    public ResponseEntity<ApiResponse<AnalysisDTO>> updateAnalysis(@PathVariable Long id, @RequestBody AnalysisRequestDTO request) {
        Optional<Analysis> analysisOpt = analysisRepository.findById(id);
        if (analysisOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Análisis no encontrado", null));
        }

        Analysis analysis = analysisOpt.get();

        if (request.getName() != null) analysis.setName(request.getName());
        if (request.getDescription() != null) analysis.setDescription(request.getDescription());
        if (request.getPrice() != null) analysis.setPrice(request.getPrice());

        if (request.getLabId() != null) {
            Optional<Lab> labOpt = labRepository.findById(request.getLabId());
            if (labOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Laboratorio no encontrado", null));
            }
            analysis.setLab(labOpt.get());
        }

        Analysis updated = analysisRepository.save(analysis);

        return ResponseEntity.ok(new ApiResponse<>(true, "Análisis actualizado correctamente", EntityMapper.toAnalysisDTO(updated)));
    }

    // Eliminar análisis
    @DeleteMapping("/{id}")
    @AllowedRoles({"ADMIN"})
    public ResponseEntity<ApiResponse<Void>> deleteAnalysis(@PathVariable Long id) {
        if (!analysisRepository.existsById(id)) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Análisis no encontrado", null));
        }

        analysisRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Análisis eliminado correctamente", null));
    }
}
