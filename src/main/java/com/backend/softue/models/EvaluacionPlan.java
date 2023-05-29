package com.backend.softue.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "Evaluacion_plan")
@JsonIgnoreProperties({"calificaciones", "planNegocio"})
public class EvaluacionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "Error: El campo 'fechaPresentacion' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la fecha de presentacion de la Evaluacion Plan.")
    @Column(nullable = false, name = "fecha_presentacion")
    private LocalDate fechaPresentacion;

    @NotNull(message = "Error: El campo 'fechaCorte' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la fecha de corte de la Evaluacion Plan.")
    @Column(nullable = false, name="fecha_corte")
    private LocalDate fechaCorte;

    @NotNull(message = "Error: El campo 'planNegocio' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el plan de negocio de la Evaluacion Plan.")
    @ManyToOne
    @JoinColumn(name = "plan_negocio", nullable = false)
    private PlanNegocio planNegocio;

    @OneToMany(mappedBy = "evaluacionPlanId", fetch = FetchType.LAZY)
    private Set<CalificacionPlan> calificaciones;
    @Transient
    private List<CalificacionPlan> calificacionesInfo;
}
