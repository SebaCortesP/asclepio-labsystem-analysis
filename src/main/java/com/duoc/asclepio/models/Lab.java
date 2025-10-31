package com.duoc.asclepio.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "labs")
public class Lab {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lab_seq")
    @SequenceGenerator(name = "lab_seq", sequenceName = "lab_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "El nombre del laboratorio es obligatorio")
    private String name;

    private String address;

    private String phone;

    // Relación uno a muchos con análisis
    @OneToMany(mappedBy = "lab", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Analysis> analyses = new ArrayList<>();

}