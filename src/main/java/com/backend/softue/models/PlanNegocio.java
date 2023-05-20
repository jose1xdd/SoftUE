package com.backend.softue.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "Plan_negocio")
public class PlanNegocio {
    @Id
    private Integer id;

    @NotBlank
    @NotNull
    @Column(nullable = false)
    private String titulo;

    @NotBlank
    private String resumen;

    @NotNull
    @Column(nullable = false)
    private Character estado;

    @NotBlank
    @NotNull
    @Column(nullable = false)
    private String areaEnfoque;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false, name = "tutor_codigo")
    private Docente tutor;
    @Transient
    private Integer codigoTutor;

    @NotNull
    @Column(nullable = false)
    private LocalDate fechaCreacion;

    @NotNull
    @Column(nullable = false, name = "Estudiante_codigo")
    //private Estudiante estudiante;
    //@Transient
    private Integer codigoEsdiante;

    @NotNull
    @Column(nullable = false, name = "documento_plan_id")
    private Integer IdDocumentoPlan;

    @OneToMany(mappedBy = "planNegocio", fetch = FetchType.LAZY)
    private Set<EvaluacionPlan> evaluaciones;

    @OneToMany(mappedBy = "planNegocio", fetch = FetchType.LAZY)
    private Set<DocenteApoyoPlan> docentesApoyo;
}
