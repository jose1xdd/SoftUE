package com.backend.softue.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "Error: El campo 'enunciado' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el enunciado de la Pregunta.")
    @Column(nullable = false)
    private String enunciado;

    @NotNull(message = "Error: El campo 'componente_competencias_id' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el componente_competencias_id de la pregunta.")
    @ManyToOne()
    @JoinColumn(name = "componente_competencias_id")
    private ComponenteCompetencias componenteCompetenciasId;

    @OneToMany(mappedBy = "preguntaId", fetch = FetchType.LAZY)
    private Set<Respuesta> respuestas;
}