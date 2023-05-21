package com.backend.softue.models;

import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codigoEstudiante")
    @JoinColumn(name = "estudiante_codigo")
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("planNegocio")
    @JoinColumn(name = "planNegocio_id")
    private PlanNegocio planNegocio;
}
