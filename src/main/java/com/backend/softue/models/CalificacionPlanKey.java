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

    @NotNull
    @Column(name = "docente_codigo")
    private Integer codigoDocente;

    @NotNull
    @Column(name = "evaluacion_plan_id")
    private Integer evaluacionPlanId;
}
