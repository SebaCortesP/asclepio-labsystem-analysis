package com.duoc.asclepio.controller;
import com.duoc.asclepio.dto.ApiResponse;
import com.duoc.asclepio.models.Lab;
import com.duoc.asclepio.repository.LabRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/labs")
public class LabController {

    private final LabRepository labRepository;

    public LabController(LabRepository labRepository) {
        this.labRepository = labRepository;
    }

    // Crear laboratorio
    @PostMapping
    public ResponseEntity<ApiResponse<Lab>> createLab(@RequestBody Lab lab) {
        if (labRepository.existsByName(lab.getName())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "El laboratorio ya existe", null));
        }
        Lab saved = labRepository.save(lab);
        return ResponseEntity.ok(new ApiResponse<>(true, "Laboratorio creado correctamente", saved));
    }

    // Obtener todos los laboratorios
    @GetMapping
    public ResponseEntity<ApiResponse<List<Lab>>> getAllLabs() {
        List<Lab> labs = labRepository.findAll();
        return ResponseEntity.ok(new ApiResponse<>(true, "Laboratorios obtenidos", labs));
    }

    // Obtener laboratorio por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Lab>> getLabById(@PathVariable Long id) {
        Optional<Lab> labOpt = labRepository.findById(id);
        if (labOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Laboratorio no encontrado", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Laboratorio obtenido", labOpt.get()));
    }

    // Actualizar laboratorio
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Lab>> updateLab(@PathVariable Long id, @RequestBody Lab lab) {
        Optional<Lab> labOpt = labRepository.findById(id);
        if (labOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Laboratorio no encontrado", null));
        }
        Lab existingLab = labOpt.get();
        existingLab.setName(lab.getName());
        existingLab.setAddress(lab.getAddress());
        existingLab.setPhone(lab.getPhone());
        Lab updatedLab = labRepository.save(existingLab);
        return ResponseEntity.ok(new ApiResponse<>(true, "Laboratorio actualizado", updatedLab));
    }

    // Eliminar laboratorio
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLab(@PathVariable Long id) {
        if (!labRepository.existsById(id)) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Laboratorio no encontrado", null));
        }
        labRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Laboratorio eliminado", null));
    }
}