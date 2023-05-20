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
    DocentePlanKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codigoDocente")
    @JoinColumn(name = "docente_codigo")
    Docente docente;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codigoDocente")
    @JoinColumn(name = "planNegocio_id")
    PlanNegocio planNegocio;
}
