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
public class DocentePlanKey implements Serializable {

    @Column(nullable = false, name = "docente_codigo")
    private Integer codigoDocente;

    @Column(nullable = false, name = "planNegocio_id")
    private Integer planNegocio;
}
