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
@Table(name = "Calificacion_idea")
@JsonIgnoreProperties({"docente", "evaluacionIdeaId"})
public class CalificacionIdea {
    @EmbeddedId
    private CalificacionIdeaKey id;

    @NotNull(message = "Error: El campo 'codigoDocente' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el codigoDocente de la calificacion de la idea.")
    @ManyToOne
    @MapsId("codigoDocente")
    @JoinColumn(nullable = false, name = "docente_codigo")
    private Docente docente;

    @NotNull(message = "Error: El campo 'evaluacionIdeaId' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el evaluacionIdeaId de la calificacion de la idea.")
    @ManyToOne
    @MapsId("evaluacionIdeaId")
    @JoinColumn(nullable = false, name = "evaluacion_idea_id")
    private EvaluacionIdea evaluacionIdeaId;

    @NotBlank(message = "Error: El campo 'estado' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el estado de la calificacion de la idea.")
    @NotNull(message = "Error: El campo 'estado' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el estado de la calificacion de la idea.")
    @Column(nullable = false)
    private String estado;

    @Column(columnDefinition = "varchar(1024)")
    private String observacion;

    @NotNull(message = "Error: El campo 'fecha_inicio' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la fecha_inicio de la calificacion de la idea.")
    @Column(nullable = false, name = "fecha_inicio")
    private LocalDate fechaInicio;

    @NotNull(message = "Error: El campo 'fecha_corte' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la fecha_corte de la calificacion de la idea.")
    @Column(nullable = false, name = "fecha_corte")
    private LocalDate fechaCorte;
}
