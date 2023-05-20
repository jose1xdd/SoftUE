package com.backend.softue.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "Evaluacion_plan")
public class EvaluacionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @Column(nullable = false, name = "fecha_presentacion")
    private LocalDate fechaPresentacion;

    @NotNull
    @Column(nullable = false, name="fecha_corte")
    private LocalDate fechaCorte;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "plan_negocio", nullable = false)
    private PlanNegocio planNegocio;
}