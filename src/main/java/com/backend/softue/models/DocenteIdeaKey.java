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
public class DocenteIdeaKey {

    @Column(name = "docente_codigo")
    private Integer codigoDocente;

    @Column(name = "ideaNegocio_id")
    private Integer ideaNegocio;
}
