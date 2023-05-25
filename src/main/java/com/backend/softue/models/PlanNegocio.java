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

    @NotBlank(message = "Error: El campo 'titulo' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el titulo del Plan de Negocio.")
    @NotNull(message = "Error: El campo 'titulo' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el titulo del Plan de Negocio.")
    @Column(nullable = false, unique = true)
    private String titulo;

    private String resumen;

    @NotNull(message = "Error: El campo 'estado' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el estado del Plan de Negocio.")
    @Column(nullable = false)
    private Character estado;

    @NotBlank(message = "Error: El campo 'areaEnfoque' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el area de enfoque del Plan de Negocio.")
    @NotNull(message = "Error: El campo 'areaEnfoque' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el area de enfoque del Plan de Negocio.")
    @Column(nullable = false)
    private String areaEnfoque;

    @ManyToOne
    @JoinColumn(nullable = false, name = "tutor_codigo")
    private Docente tutor;

    @NotNull(message = "Error: El campo 'fechaCreacion' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la fecha de creacion del Plan de Negocio.")
    @Column(nullable = false)
    private LocalDate fechaCreacion;

    @NotNull(message = "Error: El campo 'estudianteLider' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el estudiante lider del Plan de Negocio.")
    @ManyToOne
    @JoinColumn(nullable = false, name = "codigo_estudiante_lider")
    private Estudiante estudianteLider;

    @OneToOne(mappedBy = "planNegocio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private DocumentoPlan documentoPlan;

    @OneToMany(mappedBy = "planNegocio", fetch = FetchType.LAZY)
    private Set<EvaluacionPlan> evaluaciones;

    @OneToMany(mappedBy = "planNegocio", fetch = FetchType.LAZY)
    private Set<DocenteApoyoPlan> docentesApoyo;

    @OneToMany(mappedBy = "planNegocio", fetch = FetchType.LAZY)
    private Set<PlanPresentado> estudiantesIntegrantes;

    @OneToMany(mappedBy = "planNegocioId", fetch = FetchType.LAZY)
    private Set<ObservacionPlan> observaciones;
}
