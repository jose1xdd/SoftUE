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
@Table(name = "Observacion_plan")
public class ObservacionPlan {
    @EmbeddedId
    ObservacionPlanKey id;

    @NotNull(message = "Error: El campo 'planNegocioId' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el planNegocioId de la Observacion Plan.")
    @ManyToOne()
    @MapsId("planNegocioId")
    @JoinColumn(name = "plan_negocioId")
    private PlanNegocio planNegocioId;

    @NotNull(message = "Error: El campo 'docenteId' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el docenteId de la Observacion Plan.")
    @ManyToOne()
    @MapsId("docenteId")
    @JoinColumn(name = "docente_id")
    private Docente docenteId;

    @NotBlank(message = "Error: El campo 'retroalimentacion' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para la retroalimentacion de la Observacion Plan.")
    @NotNull(message = "Error: El campo 'retroalimentacion' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la retroalimentacion de la Observacion Plan.")
    @Column(nullable = false)
    private String retroalimentacion;

    @NotNull(message = "Error: El campo 'fecha' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la fecha de la Observacion Plan.")
    @Column(nullable = false)
    private LocalDateTime fecha;
}
