package com.duoc.asclepio.models;
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

    @Column(nullable = false)
    @NotBlank(message = "El nombre del análisis es obligatorio")
    private String name;

    private String description;

    private Double price;

    // Relación muchos a uno con laboratorio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id", nullable = false)
    private Lab lab;
}