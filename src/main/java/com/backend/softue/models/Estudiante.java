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
@Table(name = "Estudiante")
public class Estudiante extends User{

    @NotBlank
    @NotNull
    @Column(nullable = false)
    private String curso;

    @NotBlank
    @NotNull
    @Column(nullable = false, name = "nombre_acudiente")
    private String nombreAcudiente;

    @NotBlank
    @NotNull
    @Column(nullable = false, name = "capacitacion_aprobada")
    private String capacitacionAprobada;

    @OneToMany(mappedBy = "estudianteLider", fetch = FetchType.LAZY)
    private Set<PlanNegocio> planesNegocioLideradas;

    @OneToMany(mappedBy = "estudianteLider", fetch = FetchType.LAZY)
    private Set<IdeaNegocio> ideasNegocioLideradas;

    @OneToMany(mappedBy = "estudiante", fetch = FetchType.LAZY)
    private Set<IdeaPlanteada> ideasNegocio;

    @OneToMany(mappedBy = "estudiante", fetch = FetchType.LAZY)
    private Set<PlanPresentado> planesNegocio;
}
