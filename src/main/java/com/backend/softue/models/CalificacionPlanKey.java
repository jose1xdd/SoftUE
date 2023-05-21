package com.backend.softue.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CalificacionPlanKey implements Serializable {

    @NotNull(message = "Error: El campo 'codigoDocente' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el codigoDocente de la llave compuesta de CalificacionPlan.")
    @Column(name = "docente_codigo")
    private Integer codigoDocente;

    @NotNull(message = "Error: El campo 'evaluacionPlanId' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la evaluacionPlanId de la llave compuesta de CalificacionPlan.")
    @Column(name = "evaluacion_plan_id")
    private Integer evaluacionPlanId;
}
