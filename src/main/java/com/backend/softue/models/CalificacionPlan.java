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
@Table(name = "Calificacion_plan")
public class CalificacionPlan {
    @EmbeddedId
    private CalificacionIdeaKey id;

    @NotNull
    @ManyToOne
    @MapsId("codigoDocente")
    @JoinColumn(nullable = false, name = "docente_codigo")
    private Docente docente;

    @NotNull
    @ManyToOne
    @MapsId("evaluacionPlanId")
    @JoinColumn(nullable = false, name = "evaluacion_plan_id")
    private EvaluacionPlan evaluacionPlanId;

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
