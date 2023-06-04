package com.backend.softue.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"tutor", "estudianteLider", "documentoPlan", "evaluaciones", "estudiantesIntegrantes", "observaciones", "docentesApoyo", "area"})
@Table(name = "Plan_negocio")
public class PlanNegocio {
    @Id
    private Integer id;

    @NotBlank(message = "Error: El campo 'titulo' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el titulo del Plan de Negocio.")
    @NotNull(message = "Error: El campo 'titulo' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el titulo del Plan de Negocio.")
    @Column(nullable = false, unique = true)
    private String titulo;

    @Column(columnDefinition = "varchar(1024)")
    private String resumen;

    @NotNull(message = "Error: El campo 'estado' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el estado del Plan de Negocio.")
    @Column(nullable = false)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "area_enfoque", nullable = false)
    private AreaConocimiento area;
    @Transient
    private String areaEnfoque;

    @ManyToOne
    @JoinColumn(nullable = false, name = "tutor_codigo")
    private Docente tutor;
    @Transient
    private String [][] tutorInfo;

    @NotNull(message = "Error: El campo 'fechaCreacion' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la fecha de creacion del Plan de Negocio.")
    @Column(nullable = false)
    private LocalDate fechaCreacion;

    @Transient
    private LocalDate fechaCorte;

    @NotNull(message = "Error: El campo 'estudianteLider' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el estudiante lider del Plan de Negocio.")
    @ManyToOne
    @JoinColumn(nullable = false, name = "codigo_estudiante_lider")
    private Estudiante estudianteLider;
    @Transient
    private String [][] estudianteLiderInfo;

    @OneToOne(mappedBy = "planNegocio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private DocumentoPlan documentoPlan;

    @OneToMany(mappedBy = "planNegocio", fetch = FetchType.LAZY)
    private Set<EvaluacionPlan> evaluaciones;

    @OneToMany(mappedBy = "planNegocio", fetch = FetchType.LAZY)
    private Set<DocenteApoyoPlan> docentesApoyo;
    @Transient
    private String [][] docentesApoyoInfo;

    @OneToMany(mappedBy = "planNegocio", fetch = FetchType.LAZY)
    private Set<PlanPresentado> estudiantesIntegrantes;
    @Transient
    private String [][] estudiantesIntegrantesInfo;

    @OneToMany(mappedBy = "planNegocioId", fetch = FetchType.LAZY)
    private Set<ObservacionPlan> observaciones;
}
