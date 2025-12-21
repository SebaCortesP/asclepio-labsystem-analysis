package com.duoc.asclepio.application.controllers;

import com.duoc.asclepio.controller.PacientController;
import com.duoc.asclepio.clients.UserClient;
import com.duoc.asclepio.dto.PacientDTO;
import com.duoc.asclepio.models.Pacient;
import com.duoc.asclepio.models.Result;
import com.duoc.asclepio.repository.PacientRepository;
import com.duoc.asclepio.repository.ResultRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class PacientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PacientRepository pacientRepository;

    @Mock
    private ResultRepository resultRepository;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private PacientController pacientController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pacientController).build();
    }

    // ---------------------------------------------------------
    // CREAR PACIENTE
    // ---------------------------------------------------------
    // @Test
    // void testCreatePacient_ok() throws Exception {

    //     PacientDTO request = new PacientDTO();
    //     request.setUserId(10L);
    //     request.setFirstName("Juan");
    //     request.setLastName("Pérez");
    //     request.setEmail("juan@example.com");
    //     request.setBirthDate(LocalDate.of(1990, 1, 1));
    //     request.setPhone("987654321");
    //     request.setAddress("Av. Uno 123");

    //     Pacient saved = new Pacient();
    //     saved.setId(1L);
    //     saved.setUserId(10L);
    //     saved.setFirstName("Juan");
    //     saved.setLastName("Pérez");
    //     saved.setEmail("juan@example.com");
    //     saved.setBirthDate(LocalDate.of(1990, 1, 1));
    //     saved.setPhone("987654321");
    //     saved.setAddress("Av. Uno 123");

    //     Mockito.when(pacientRepository.save(any())).thenReturn(saved);

    //     mockMvc.perform(post("/pacients")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(request))
    //     )
    //     .andExpect(status().isOk())
    //     .andExpect(jsonPath("$.success").value(true))
    //     .andExpect(jsonPath("$.data.firstName").value("Juan"))
    //     .andExpect(jsonPath("$.data.email").value("juan@example.com"));
    // }

    // ---------------------------------------------------------
    // LISTAR TODOS
    // ---------------------------------------------------------
    @Test
    void testGetAllPacients_ok() throws Exception {

        Pacient p = new Pacient();
        p.setId(5L);
        p.setUserId(22L);
        p.setFirstName("Ana");
        p.setLastName("Gómez");
        p.setEmail("ana@example.com");
        p.setBirthDate(LocalDate.of(1985, 5, 5));
        p.setPhone("5551234");
        p.setAddress("Calle Falsa 123");

        Mockito.when(pacientRepository.findAll()).thenReturn(List.of(p));

        mockMvc.perform(get("/pacients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].firstName").value("Ana"));
    }

    // ---------------------------------------------------------
    // BUSCAR POR USER ID
    // ---------------------------------------------------------
    @Test
    void testGetByUser_found() throws Exception {

        Pacient p = new Pacient();
        p.setId(10L);
        p.setUserId(99L);
        p.setFirstName("Carlos");
        p.setLastName("Lopez");
        p.setEmail("carlos@example.com");
        p.setBirthDate(LocalDate.of(1980, 2, 2));
        p.setPhone("999888777");
        p.setAddress("Av. Sur 999");

        Mockito.when(pacientRepository.findByUserId(99L)).thenReturn(p);

        mockMvc.perform(get("/pacients/by-user/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Carlos"));
    }

    @Test
    void testGetByUser_notFound() throws Exception {

        Mockito.when(pacientRepository.findByUserId(77L)).thenReturn(null);

        mockMvc.perform(get("/pacients/by-user/77"))
                .andExpect(status().isNotFound());
    }

    // ---------------------------------------------------------
    // OBTENER RESULTADOS
    // ---------------------------------------------------------
    // @Test
    // void testGetResultsByPacient_ok() throws Exception {

    //     Result r = new Result();
    //     r.setId(7L);
    //     r.setUserId(50L);
    //     r.setAnalysis(34);
    //     r.setValue("Positivo");
    //     r.setObservation("Todo bien");

    //     Mockito.when(resultRepository.findByUserId(50L)).thenReturn(List.of(r));

    //     mockMvc.perform(get("/pacients/50/results"))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$[0].value").value("Positivo"))
    //             .andExpect(jsonPath("$[0].analysisId").value(44L));
    // }

    @Test
    void testGetResultsByPacient_empty() throws Exception {

        Mockito.when(resultRepository.findByUserId(123L)).thenReturn(List.of());

        mockMvc.perform(get("/pacients/123/results"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
