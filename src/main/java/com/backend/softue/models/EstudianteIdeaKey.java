package com.backend.softue.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Embeddable
public class EstudianteIdeaKey implements Serializable {

    @Column(nullable = false, name = "estudiante_codigo")
    private Integer codigoEstudiante;

    @Column(nullable = false, name = "ideaNegocio_id")
    private Integer ideaNegocio;
}
