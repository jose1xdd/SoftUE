package com.backend.softue.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Error: El campo 'estudiante' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el estudiante de la IdeaPlanteada.")
    @ManyToOne()
    @MapsId("codigoEstudiante")
    @JoinColumn(name = "estudiante_codigo")
    private Estudiante estudiante;

    @NotNull(message = "Error: El campo 'ideaNegocio' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la ideaNegocio de la IdeaPlanteada.")
    @ManyToOne()
    @MapsId("ideaNegocio")
    @JoinColumn(name = "ideaNegocio_id")
    private IdeaNegocio ideaNegocio;
}
