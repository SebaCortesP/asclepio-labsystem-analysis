package com.duoc.asclepio.controller;

import com.duoc.asclepio.clients.UserClient;
import com.duoc.asclepio.dto.ApiResponse;
import com.duoc.asclepio.dto.PacientDTO;
import com.duoc.asclepio.models.Pacient;
import com.duoc.asclepio.repository.PacientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/pacients")
public class PacientController {

    private final PacientRepository pacientRepository;
    private final UserClient userClient;

    public PacientController(PacientRepository pacientRepository, UserClient userClient) {
        this.pacientRepository = pacientRepository;
        this.userClient = userClient;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PacientDTO>> createPacient(@RequestBody PacientDTO dto) {
        if (!userClient.userExists(dto.getUserId())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "El usuario no existe en el servicio de Apolo", null));
        }

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
}
