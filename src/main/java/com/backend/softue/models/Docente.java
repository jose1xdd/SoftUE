package com.backend.softue.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "Docente")
public class Docente extends User {

    @NotBlank(message = "Error: El campo 'cedula' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para la cedula del Docente.")
    @NotNull(message = "Error: El campo 'cedula' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la cedula del Docente.")
    @Column(nullable = false)
    private String cedula;

    @NotBlank(message = "Error: El campo 'titulo' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el titulo del Docente.")
    @NotNull(message = "Error: El campo 'titulo' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el titulo del Docente.")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "Error: El campo 'area' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el area especializada del Docente.")
    @NotNull(message = "Error: El campo 'area' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el area especializada del Docente.")
    @Column(nullable = false)
    private String area;

    @OneToMany(mappedBy = "docente", fetch = FetchType.LAZY)
    private Set<DocenteApoyoPlan> planesApoyados;

    @OneToMany(mappedBy = "docente", fetch = FetchType.LAZY)
    private Set<DocenteApoyoIdea> ideasApoyadas;

    @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY)
    private Set<IdeaNegocio> ideasTutoradas;

    @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY)
    private Set<PlanNegocio> planesTutoradas;

    @OneToMany(mappedBy = "docenteId", fetch = FetchType.LAZY)
    private Set<ObservacionPlan> observacionesPlan;

    @OneToMany(mappedBy = "docenteId", fetch = FetchType.LAZY)
    private Set<ObservacionIdea> observacionesIdea;

    @OneToMany(mappedBy = "docente", fetch = FetchType.LAZY)
    private Set<CalificacionIdea> ideasCalificadas;

    @OneToMany(mappedBy = "docente", fetch = FetchType.LAZY)
    private Set<CalificacionPlan> planesCalificados;


}
