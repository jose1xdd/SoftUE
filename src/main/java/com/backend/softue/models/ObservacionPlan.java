package com.backend.softue.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class ObservacionPlan {
    @EmbeddedId
    ObservacionPlanKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("planNegocioId")
    @JoinColumn(name = "plan_negocioId")
    private PlanNegocio planNegocioId;


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("docenteId")
    @JoinColumn(name = "docente_id")
    private Docente docenteId;

    @NotBlank
    @NotNull
    @Column(nullable = false)
    private String retroalimentacion;

    @NotBlank
    @NotNull
    @Column(nullable = false)
    private LocalDateTime fecha;
}
