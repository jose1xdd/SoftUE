package com.backend.softue.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "Idea_planteada")
public class IdeaPlanteada {

    @EmbeddedId
    private EstudianteIdeaKey id;

    @ManyToOne()
    @MapsId("codigoEstudiante")
    @JoinColumn(name = "estudiante_codigo")
    private Estudiante estudiante;

    @ManyToOne()
    @MapsId("ideaNegocio")
    @JoinColumn(name = "ideaNegocio_id")
    private IdeaNegocio ideaNegocio;
}
