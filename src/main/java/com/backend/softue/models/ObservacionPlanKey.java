package com.backend.softue.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Embeddable
public class ObservacionPlanKey implements Serializable {

    @NotNull(message = "Error: El campo 'planNegocioId' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el planNegocioId de la llave compuesta de ObservacionPlan.")
    @Column(name = "plan_id")
    private Integer planNegocioId;

    @NotNull(message = "Error: El campo 'docenteId' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el docenteId de la llave compuesta de ObservacionPlan.")
    @Column(name = "docente_id")
    private Integer docenteId;
}
