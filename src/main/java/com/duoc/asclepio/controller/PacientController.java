package com.duoc.asclepio.controller;

import com.duoc.asclepio.clients.UserClient;
import com.duoc.asclepio.dto.ApiResponse;
import com.duoc.asclepio.dto.PacientDTO;
import com.duoc.asclepio.dto.ResultDTO;
import com.duoc.asclepio.mappers.PacientMapper;
import com.duoc.asclepio.models.Pacient;
import com.duoc.asclepio.models.Result;
import com.duoc.asclepio.repository.PacientRepository;
import com.duoc.asclepio.repository.ResultRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pacients")
public class PacientController {

    private final PacientRepository pacientRepository;
    private final ResultRepository resultRepository;
    private final UserClient userClient;

    public PacientController(PacientRepository pacientRepository, ResultRepository resultRepository, UserClient userClient) {
        this.pacientRepository = pacientRepository;
        this.resultRepository = resultRepository;
        this.userClient = userClient;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PacientDTO>> createPacient(@RequestBody PacientDTO dto) {
        Pacient pacient = new Pacient();
        pacient.setUserId(dto.getUserId());
        pacient.setFirstName(dto.getFirstName());
        pacient.setLastName(dto.getLastName());
        pacient.setEmail(dto.getEmail());
        pacient.setBirthDate(dto.getBirthDate());
        pacient.setPhone(dto.getPhone());
        pacient.setUserId(dto.getUserId());
        pacient.setAddress(dto.getAddress());

        pacientRepository.save(pacient);
        dto.setId(pacient.getId());

        return ResponseEntity.ok(new ApiResponse<>(true, "Paciente creado correctamente", dto));
    }

    @GetMapping("/{id}/results")
    public ResponseEntity<List<ResultDTO>> getResultsByPacient(@PathVariable Long id) {
        List<Result> results = resultRepository.findByUserId(id);

        List<ResultDTO> dtos = results.stream().map(ResultDTO::fromEntity).toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PacientDTO>>> getAllPacients() {
        List<Pacient> pacients = pacientRepository.findAll();

        List<PacientDTO> dtos = pacients.stream()
                .map(p -> new PacientDTO(
                        p.getId(),
                        p.getUserId(),
                        p.getFirstName(),
                        p.getLastName(),
                        p.getEmail(),
                        p.getBirthDate(),
                        p.getPhone(),
                        p.getAddress()
                ))
                .toList();

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Lista de pacientes obtenida correctamente", dtos)
        );
    }


    @GetMapping("/by-user/{userId}")
    public ResponseEntity<PacientDTO> getByUser(@PathVariable Long userId) {
        Pacient p = pacientRepository.findByUserId(userId);

        if (p == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(PacientMapper.toDto(p));
    }
}
