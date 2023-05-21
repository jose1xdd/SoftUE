package com.backend.softue.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Embeddable
public class EstudiantePlanKey implements Serializable {

    @NotNull(message = "Error: El campo 'codigoEstudiante' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el codigoEstudiante de la llave compuesta de los estudiantes integrantes del plan de negocio (EstudiantePlanKey).")
    @Column(nullable = false, name = "estudiante_codigo")
    private Integer codigoEstudiante;

    @NotNull(message = "Error: El campo 'planNegocio' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el planNegocio de la llave compuesta de los estudiantes integrantes del plan de negocio (EstudiantePlanKey).")
    @Column(nullable = false, name = "planNegocio_id")
    private Integer planNegocio;
}
