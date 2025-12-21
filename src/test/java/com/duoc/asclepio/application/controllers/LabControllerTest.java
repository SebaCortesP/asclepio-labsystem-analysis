package com.duoc.asclepio.application.controllers;

import com.duoc.asclepio.controller.LabController;
import com.duoc.asclepio.dto.LabRequestDTO;
import com.duoc.asclepio.models.Lab;
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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LabControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LabRepository labRepository;

    @InjectMocks
    private LabController labController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(labController).build();
    }

    // ---------------------------------------------------------
    // CREAR LABORATORIO
    // ---------------------------------------------------------
    @Test
    void testCreateLab_ok() throws Exception {

        LabRequestDTO request = new LabRequestDTO();
        request.setName("Laboratorio Alfa");
        request.setAddress("Av. Central 123");
        request.setPhone("987654321");

        Lab saved = new Lab();
        saved.setId(1L);
        saved.setName("Laboratorio Alfa");
        saved.setAddress("Av. Central 123");
        saved.setPhone("987654321");

        Mockito.when(labRepository.existsByName("Laboratorio Alfa")).thenReturn(false);
        Mockito.when(labRepository.save(any())).thenReturn(saved);

        mockMvc.perform(post("/labs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.name").value("Laboratorio Alfa"))
        .andExpect(jsonPath("$.data.address").value("Av. Central 123"));
    }

    @Test
    void testCreateLab_duplicateName() throws Exception {

        LabRequestDTO request = new LabRequestDTO();
        request.setName("Duplicado");
        request.setAddress("Calle X");
        request.setPhone("555");

        Mockito.when(labRepository.existsByName("Duplicado")).thenReturn(true);

        mockMvc.perform(post("/labs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("El laboratorio ya existe"));
    }

    // ---------------------------------------------------------
    // LISTAR TODOS
    // ---------------------------------------------------------
    @Test
    void testGetAllLabs_ok() throws Exception {

        Lab lab = new Lab();
        lab.setId(10L);
        lab.setName("Lab Norte");
        lab.setAddress("Calle 100");
        lab.setPhone("111222333");

        Mockito.when(labRepository.findAll()).thenReturn(List.of(lab));

        mockMvc.perform(get("/labs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").value("Lab Norte"));
    }

    // ---------------------------------------------------------
    // OBTENER POR ID
    // ---------------------------------------------------------
    @Test
    void testGetLabById_found() throws Exception {

        Lab lab = new Lab();
        lab.setId(5L);
        lab.setName("Lab Sur");
        lab.setAddress("Av. Sur 777");
        lab.setPhone("999111222");

        Mockito.when(labRepository.findById(5L)).thenReturn(Optional.of(lab));

        mockMvc.perform(get("/labs/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Lab Sur"));
    }

    @Test
    void testGetLabById_notFound() throws Exception {

        Mockito.when(labRepository.findById(77L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/labs/77"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Laboratorio no encontrado"));
    }

    // ---------------------------------------------------------
    // ACTUALIZAR LABORATORIO
    // ---------------------------------------------------------
    @Test
    void testUpdateLab_ok() throws Exception {

        LabRequestDTO request = new LabRequestDTO();
        request.setName("Lab Actualizado");
        request.setAddress("Nueva Dirección 555");
        request.setPhone("123123123");

        Lab existing = new Lab();
        existing.setId(4L);
        existing.setName("Lab Viejo");
        existing.setAddress("Dirección vieja");
        existing.setPhone("000111222");

        Lab updated = new Lab();
        updated.setId(4L);
        updated.setName("Lab Actualizado");
        updated.setAddress("Nueva Dirección 555");
        updated.setPhone("123123123");

        Mockito.when(labRepository.findById(4L)).thenReturn(Optional.of(existing));
        Mockito.when(labRepository.save(any())).thenReturn(updated);

        mockMvc.perform(put("/labs/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.name").value("Lab Actualizado"));
    }

    @Test
    void testUpdateLab_notFound() throws Exception {

        LabRequestDTO request = new LabRequestDTO();
        request.setName("No Existe");
        request.setAddress("X");
        request.setPhone("X");

        Mockito.when(labRepository.findById(22L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/labs/22")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Laboratorio no encontrado"));
    }

    // ---------------------------------------------------------
    // ELIMINAR LABORATORIO
    // ---------------------------------------------------------
    @Test
    void testDeleteLab_ok() throws Exception {

        Mockito.when(labRepository.existsById(100L)).thenReturn(true);

        mockMvc.perform(delete("/labs/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Laboratorio eliminado"));
    }

    @Test
    void testDeleteLab_notFound() throws Exception {

        Mockito.when(labRepository.existsById(100L)).thenReturn(false);

        mockMvc.perform(delete("/labs/100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Laboratorio no encontrado"));
    }
}
