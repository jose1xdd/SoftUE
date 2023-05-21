package com.backend.softue.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "Docente_apoyo_plan")
public class DocenteApoyoPlan {

    @EmbeddedId
    private DocentePlanKey id;

    @ManyToOne()
    @MapsId("codigoDocente")
    @JoinColumn(name = "docente_codigo")
    private Docente docente;

    @ManyToOne()
    @MapsId("planNegocio")
    @JoinColumn(name = "planNegocio_id")
    private PlanNegocio planNegocio;
}
