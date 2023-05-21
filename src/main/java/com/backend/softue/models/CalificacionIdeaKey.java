package com.backend.softue.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CalificacionIdeaKey implements Serializable {

    @NotNull(message = "Error: El campo 'codigoDocente' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el codigoDocente de la llave compuesta de CalificacionIdea.")
    @Column(name = "docente_codigo")
    private Integer codigoDocente;

    @NotNull(message = "Error: El campo 'evaluacionIdeaId' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la evaluacionIdeaId de la llave compuesta de CalificacionIdea.")
    @Column(name = "evaluacion_idea_id")
    private Integer evaluacionIdeaId;
}
