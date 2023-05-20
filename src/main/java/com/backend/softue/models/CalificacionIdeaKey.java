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
public class CalificacionIdeaKey implements Serializable {

    @NotNull
    @Column(name = "docente_codigo")
    private Integer codigoDocente;

    @NotNull
    @Column(name = "evaluacion_idea_id")
    private Integer evaluacionIdeaId;
}
