package com.backend.softue.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Embeddable
public class EstudiantePlanKey {

    @Column(nullable = false, name = "estudiante_codigo")
    private Integer codigoEstudiante;

    @Column(nullable = false, name = "planNegocio_id")
    private Integer planNegocio;
}