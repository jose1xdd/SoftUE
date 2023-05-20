package com.backend.softue.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "DocenteApoyoIdea")
public class DocenteApoyoIdea {

    @EmbeddedId
    private DocenteIdeaKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codigoDocente")
    @JoinColumn(name = "docente_codigo")
    private Docente docente;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ideaNegocio")
    @JoinColumn(name = "ideaNegocio_id")
    private IdeaNegocio ideaNegocio;
}
