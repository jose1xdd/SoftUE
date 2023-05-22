package com.backend.softue.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"foto_usuario","planesNegocioLideradas", "ideasNegocioLideradas", "ideasNegocio", "planesNegocio"})
public class Estudiante extends User{

    @NotBlank(message = "Error: El campo 'curso' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el curso del Estudiante.")
    @NotNull(message = "Error: El campo 'curso' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el curso del Estudiante.")
    @Column(nullable = false)
    private String curso;

    @NotBlank(message = "Error: El campo 'nombreAcudiente' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el nombre del acudiente del Estudiante.")
    @NotNull(message = "Error: El campo 'nombreAcudiente' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el nombre del acudiente del Estudiante.")
    @Column(nullable = false, name = "nombre_acudiente")
    private String nombreAcudiente;

    @NotBlank(message = "Error: El campo 'capacitacionAprobada' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para la capacitacion aprobada del Estudiante.")
    @NotNull(message = "Error: El campo 'capacitacionAprobada' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la capacitacion aprobada del Estudiante.")
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
