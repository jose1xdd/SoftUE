package com.backend.softue.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "Docente_apoyo_idea")
public class DocenteApoyoIdea {

    @EmbeddedId
    private DocenteIdeaKey id;

    @ManyToOne()
    @MapsId("codigoDocente")
    @JoinColumn(name = "docente_codigo")
    private Docente docente;

    @ManyToOne()
    @MapsId("ideaNegocio")
    @JoinColumn(name = "ideaNegocio_id")
    private IdeaNegocio ideaNegocio;
}
