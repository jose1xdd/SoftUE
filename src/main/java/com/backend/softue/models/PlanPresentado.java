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

    @ManyToOne()
    @MapsId("codigoEstudiante")
    @JoinColumn(name = "estudiante_codigo")
    private Estudiante estudiante;

    @ManyToOne()
    @MapsId("planNegocio")
    @JoinColumn(name = "planNegocio_id")
    private PlanNegocio planNegocio;
}
