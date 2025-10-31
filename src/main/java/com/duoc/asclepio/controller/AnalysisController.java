package com.duoc.asclepio.controller;
import com.duoc.asclepio.dto.ApiResponse;
import com.duoc.asclepio.models.Analysis;
import com.duoc.asclepio.models.Lab;
import com.duoc.asclepio.repository.AnalysisRepository;
import com.duoc.asclepio.repository.LabRepository;
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

    @PostMapping
    public ResponseEntity<ApiResponse<Analysis>> createAnalysis(@RequestParam Long labId, @RequestBody Analysis analysis) {
        Optional<Lab> labOpt = labRepository.findById(labId);
        if (labOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Laboratorio no encontrado", null));
        }
        Lab lab = labOpt.get();

        if (analysisRepository.existsByNameAndLab(analysis.getName(), lab)) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "El análisis ya existe en este laboratorio", null));
        }

        analysis.setLab(lab);
        Analysis savedAnalysis = analysisRepository.save(analysis);
        return ResponseEntity.ok(new ApiResponse<>(true, "Análisis creado", savedAnalysis));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Analysis>>> getAllAnalyses() {
        List<Analysis> analyses = analysisRepository.findAll();
        return ResponseEntity.ok(new ApiResponse<>(true, "Análisis obtenidos", analyses));
    }

    // Obtener análisis por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Analysis>> getAnalysisById(@PathVariable Long id) {
        Optional<Analysis> analysisOpt = analysisRepository.findById(id);
        if (analysisOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Análisis no encontrado", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Análisis obtenido", analysisOpt.get()));
    }

    @GetMapping("/by-lab/{labId}")
    public ResponseEntity<ApiResponse<List<Analysis>>> getAnalysesByLab(@PathVariable Long labId) {
        Optional<Lab> labOpt = labRepository.findById(labId);
        if (labOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Laboratorio no encontrado", null));
        }
        List<Analysis> analyses = analysisRepository.findByLab(labOpt.get());
        return ResponseEntity.ok(new ApiResponse<>(true, "Análisis del laboratorio obtenidos", analyses));
    }

    // Actualizar análisis
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Analysis>> updateAnalysis(@PathVariable Long id, @RequestBody Analysis analysis) {
        Optional<Analysis> analysisOpt = analysisRepository.findById(id);
        if (analysisOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Análisis no encontrado", null));
        }
        Analysis existingAnalysis = analysisOpt.get();
        existingAnalysis.setName(analysis.getName());
        existingAnalysis.setPrice(analysis.getPrice());
        Analysis updatedAnalysis = analysisRepository.save(existingAnalysis);
        return ResponseEntity.ok(new ApiResponse<>(true, "Análisis actualizado", updatedAnalysis));
    }

    // Eliminar análisis
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAnalysis(@PathVariable Long id) {
        if (!analysisRepository.existsById(id)) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Análisis no encontrado", null));
        }
        analysisRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Análisis eliminado", null));
    }
}