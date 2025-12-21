package com.duoc.asclepio.application.controllers;

import com.duoc.asclepio.controller.AnalysisController;
import com.duoc.asclepio.dto.AnalysisRequestDTO;
import com.duoc.asclepio.models.Analysis;
import com.duoc.asclepio.models.Lab;
import com.duoc.asclepio.repository.AnalysisRepository;
import com.duoc.asclepio.repository.LabRepository;
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

import java.util.Optional;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;

class AnalysisControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AnalysisRepository analysisRepository;

    @Mock
    private LabRepository labRepository;

    @InjectMocks
    private AnalysisController analysisController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(analysisController).build();
    }

    // ---------------------------------------------------------
    // TEST: CREAR ANÁLISIS
    // ---------------------------------------------------------
    @Test
    void testCreateAnalysis_ok() throws Exception {

        AnalysisRequestDTO request = new AnalysisRequestDTO();
        request.setName("Hemograma");
        request.setDescription("Examen de sangre completo");
        request.setPrice(15000.0);
        request.setLabId(1L);

        Lab lab = new Lab();
        lab.setId(1L);
        lab.setName("Laboratorio Central");

        Analysis saved = new Analysis();
        saved.setId(10L);
        saved.setName(request.getName());
        saved.setDescription(request.getDescription());
        saved.setPrice(request.getPrice());
        saved.setLab(lab);

        Mockito.when(labRepository.findById(1L)).thenReturn(Optional.of(lab));
        Mockito.when(analysisRepository.existsByNameAndLab(anyString(), any())).thenReturn(false);
        Mockito.when(analysisRepository.save(any())).thenReturn(saved);

        mockMvc.perform(
                post("/analyses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.name").value("Hemograma"));
    }

    // ---------------------------------------------------------
    // TEST: LISTAR TODOS
    // ---------------------------------------------------------
    @Test
    void testGetAllAnalyses_ok() throws Exception {

        Analysis a = new Analysis();
        a.setId(5L);
        a.setName("Perfil Lipídico");
        a.setPrice(20000.0);

        Mockito.when(analysisRepository.findAll()).thenReturn(List.of(a));

        mockMvc.perform(get("/analyses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Perfil Lipídico"));
    }

    // ---------------------------------------------------------
    // TEST: OBTENER POR ID
    // ---------------------------------------------------------
    @Test
    void testGetAnalysisById_found() throws Exception {

        Analysis a = new Analysis();
        a.setId(99L);
        a.setName("Glucosa");

        Mockito.when(analysisRepository.findById(99L)).thenReturn(Optional.of(a));

        mockMvc.perform(get("/analyses/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Glucosa"));
    }

    @Test
    void testGetAnalysisById_notFound() throws Exception {

        Mockito.when(analysisRepository.findById(123L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/analyses/123"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    // ---------------------------------------------------------
    // TEST: ELIMINAR ANALYSIS
    // ---------------------------------------------------------
    @Test
    void testDeleteAnalysis_ok() throws Exception {

        Mockito.when(analysisRepository.existsById(20L)).thenReturn(true);

        mockMvc.perform(delete("/analyses/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testDeleteAnalysis_notFound() throws Exception {

        Mockito.when(analysisRepository.existsById(20L)).thenReturn(false);

        mockMvc.perform(delete("/analyses/20"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}