package com.duoc.asclepio.controller;

import com.duoc.asclepio.dto.*;
import com.duoc.asclepio.models.Lab;
import com.duoc.asclepio.repository.LabRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/labs")
public class LabController {

    private final LabRepository labRepository;

    public LabController(LabRepository labRepository) {
        this.labRepository = labRepository;
    }

    // Crear laboratorio
    @PostMapping
    public ResponseEntity<ApiResponse<LabDTO>> createLab(@RequestBody LabRequestDTO dto) {
        if (labRepository.existsByName(dto.getName())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "El laboratorio ya existe", null));
        }

        Lab lab = new Lab();
        lab.setName(dto.getName());
        lab.setAddress(dto.getAddress());
        lab.setPhone(dto.getPhone());

        Lab saved = labRepository.save(lab);

        LabDTO response = new LabDTO(saved.getId(), saved.getName(), saved.getAddress(), saved.getPhone());
        return ResponseEntity.ok(new ApiResponse<>(true, "Laboratorio creado correctamente", response));
    }

    // Obtener todos los laboratorios
    @GetMapping
    public ResponseEntity<ApiResponse<List<LabDTO>>> getAllLabs() {
        List<LabDTO> labs = labRepository.findAll().stream()
                .map(l -> new LabDTO(l.getId(), l.getName(), l.getAddress(), l.getPhone()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, "Laboratorios obtenidos", labs));
    }

    // Obtener laboratorio por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LabDTO>> getLabById(@PathVariable Long id) {
        Optional<Lab> labOpt = labRepository.findById(id);
        if (labOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Laboratorio no encontrado", null));
        }

        Lab lab = labOpt.get();
        LabDTO dto = new LabDTO(lab.getId(), lab.getName(), lab.getAddress(), lab.getPhone());
        return ResponseEntity.ok(new ApiResponse<>(true, "Laboratorio obtenido", dto));
    }

    // Actualizar laboratorio
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LabDTO>> updateLab(@PathVariable Long id, @RequestBody LabRequestDTO dto) {
        Optional<Lab> labOpt = labRepository.findById(id);
        if (labOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Laboratorio no encontrado", null));
        }

        Lab lab = labOpt.get();
        lab.setName(dto.getName());
        lab.setAddress(dto.getAddress());
        lab.setPhone(dto.getPhone());

        Lab updated = labRepository.save(lab);

        LabDTO response = new LabDTO(updated.getId(), updated.getName(), updated.getAddress(), updated.getPhone());
        return ResponseEntity.ok(new ApiResponse<>(true, "Laboratorio actualizado", response));
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
