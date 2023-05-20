package com.backend.softue.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "DocenteApoyoPlan")
public class DocenteApoyoPlan {

    @EmbeddedId
    private DocentePlanKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codigoDocente")
    @JoinColumn(name = "docente_codigo")
    private Docente docente;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("planNegocio")
    @JoinColumn(name = "planNegocio_id")
    private PlanNegocio planNegocio;
}
