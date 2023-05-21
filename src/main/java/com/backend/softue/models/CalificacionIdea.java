package com.backend.softue.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "Calificacion_idea")
public class CalificacionIdea {
    @EmbeddedId
    private CalificacionIdeaKey id;

    @NotNull
    @ManyToOne
    @MapsId("codigoDocente")
    @JoinColumn(nullable = false, name = "docente_codigo")
    private Docente docente;

    @NotNull
    @ManyToOne
    @MapsId("evaluacionIdeaId")
    @JoinColumn(nullable = false, name = "evaluacion_idea_id")
    private EvaluacionIdea evaluacionIdeaId;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String estado;

    @NotNull
    @NotBlank
    private String observacion;

    @NotNull
    @Column(nullable = false, name = "fecha_inicio")
    private LocalDate fechaInicio;

    @NotNull
    @Column(nullable = false, name = "fecha_corte")
    private LocalDate fechaCorte;
}
