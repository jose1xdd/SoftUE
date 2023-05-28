package com.backend.softue.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"docente", "evaluacionPlanId"})
public class CalificacionPlan {
    @EmbeddedId
    private CalificacionPlanKey id;

    @NotNull(message = "Error: El campo 'codigoDocente' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el codigoDocente de la calificacion del plan.")
    @ManyToOne
    @MapsId("codigoDocente")
    @JoinColumn(nullable = false, name = "docente_codigo")
    private Docente docente;
    @Transient
    private String nombreDocente;

    @NotNull(message = "Error: El campo 'evaluacionPlanId' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la evaluacionPlanId de la calificacion del plan.")
    @ManyToOne
    @MapsId("evaluacionPlanId")
    @JoinColumn(nullable = false, name = "evaluacion_plan_id")
    private EvaluacionPlan evaluacionPlanId;

    @NotBlank(message = "Error: El campo 'estado' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el estado de la calificacion del plan.")
    @NotNull(message = "Error: El campo 'estado' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el estado de la calificacion del plan.")
    @Column(nullable = false)
    private String estado;

    @NotBlank(message = "Error: El campo 'observacion' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para la observacion de la calificacion del plan.")
    @NotNull(message = "Error: El campo 'observacion' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la observacion de la calificacion del plan.")
    private String observacion;

    @NotNull(message = "Error: El campo 'fecha_inicio' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la fecha_inicio de la calificacion del plan.")
    @Column(nullable = false, name = "fecha_inicio")
    private LocalDate fechaInicio;

    @NotNull(message = "Error: El campo 'fecha_corte' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la fecha_corte de la calificacion del plan.")
    @Column(nullable = false, name = "fecha_corte")
    private LocalDate fechaCorte;
}
