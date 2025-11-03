package com.duoc.asclepio.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "analyses")
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysis_seq")
    @SequenceGenerator(name = "analysis_seq", sequenceName = "analysis_seq", allocationSize = 1)
    private Long id;

    @NotBlank(message = "El nombre del análisis es obligatorio")
    @Column(nullable = false)
    private String name;

    private String description;

    private Double price;

    // Relación con paciente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pacient_id")
    @JsonBackReference
    private Pacient pacient;

    // Relación con laboratorio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id", nullable = false)
    private Lab lab;
}
