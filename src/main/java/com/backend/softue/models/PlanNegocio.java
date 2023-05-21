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

    @NotNull
    @Column(nullable = false)
    private LocalDate fechaCreacion;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false, name = "codigo_estudiante_lider")
    private Estudiante estudianteLider;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_plan_id", referencedColumnName = "documento_plan_id")
    private DocumentoPlan documentoPlan;
    @Transient
    private Integer idDocumentoPlan;

    @OneToMany(mappedBy = "planNegocio", fetch = FetchType.LAZY)
    private Set<EvaluacionPlan> evaluaciones;

    @OneToMany(mappedBy = "planNegocio", fetch = FetchType.LAZY)
    private Set<DocenteApoyoPlan> docentesApoyo;

    @OneToMany(mappedBy = "planNegocio", fetch = FetchType.LAZY)
    private Set<PlanPresentado> estudiantesIntegrantes;

    @OneToMany(mappedBy = "planNegocioId", fetch = FetchType.LAZY)
    private Set<ObservacionPlan> observaciones;
}
