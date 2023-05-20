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

    @NotBlank
    @NotNull
    @Column(nullable = false)
    private String cedula;

    @NotBlank
    @NotNull
    @Column(nullable = false)
    private String titulo;

    @NotBlank
    @NotNull
    @Column(nullable = false)
    private String area;

    @OneToMany(mappedBy = "docente", fetch = FetchType.LAZY)
    private Set<DocenteApoyoPlan> docentesApoyoPlan;

    @OneToMany(mappedBy = "docente", fetch = FetchType.LAZY)
    private Set<DocenteApoyoIdea> docentesApoyoIdea;

    @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY)
    private Set<IdeaNegocio> ideasTutoradas;

    @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY)
    private Set<PlanNegocio> planesTutoradas;

    @OneToMany(mappedBy = "docente", fetch = FetchType.LAZY)
    private Set<CalificacionIdea> ideasCalificadas;

    @OneToMany(mappedBy = "docenteId", fetch = FetchType.LAZY)
    private Set<ObservacionPlan> observacionesPlan;

    @OneToMany(mappedBy = "docente", fetch = FetchType.LAZY)
    private Set<CalificacionPlan> planesCalificados;

}
