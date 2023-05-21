package com.backend.softue.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class ObservacionPlanKey implements Serializable {
    @Column(name = "plan_id")
    private Integer planNegocioId;

    @Column(name = "docente_id")
    private Integer docenteId;
}
