package com.backend.softue.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Embeddable
public class ObservacionPlanKey implements Serializable {
    @Column(name = "plan_id")
    private Integer planNegocioId;

    @Column(name = "docente_id")
    private Integer docenteId;
}
