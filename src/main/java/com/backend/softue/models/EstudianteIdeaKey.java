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
public class EstudianteIdeaKey implements Serializable {

    @NotNull(message = "Error: El campo 'codigoEstudiante' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el codigoEstudiante de la llave compuesta de los estudiantes integrantes de la idea de negocio (EstudianteIdeaKey).")
    @Column(nullable = false, name = "estudiante_codigo")
    private Integer codigoEstudiante;

    @NotNull(message = "Error: El campo 'ideaNegocio' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la ideaNegocio de la llave compuesta de los estudiantes integrantes de la idea de negocio (EstudianteIdeaKey).")
    @Column(nullable = false, name = "ideaNegocio_id")
    private Integer ideaNegocio;
}
