package com.backend.softue.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "Plan_presentado")
public class PlanPresentado {

    @EmbeddedId
    private EstudiantePlanKey id;

    @NotNull(message = "Error: El campo 'estudiante' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el estudiante del Plan Presentado.")
    @ManyToOne()
    @MapsId("codigoEstudiante")
    @JoinColumn(name = "estudiante_codigo")
    private Estudiante estudiante;

    @NotNull(message = "Error: El campo 'planNegocio' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el planNegocio del Plan Presentado.")
    @ManyToOne()
    @MapsId("planNegocio")
    @JoinColumn(name = "planNegocio_id")
    private PlanNegocio planNegocio;
}
