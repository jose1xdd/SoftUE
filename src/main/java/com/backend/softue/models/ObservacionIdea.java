package com.backend.softue.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "Observacion_idea")
public class ObservacionIdea {
    @EmbeddedId
    ObservacionIdeaKey id;

    @ManyToOne()
    @MapsId("ideaNegocioId")
    @JoinColumn(name = "idea_negocio_Id")
    private IdeaNegocio ideaNegocioId;

    @ManyToOne()
    @MapsId("docenteId")
    @JoinColumn(name = "docente_id")
    private Docente docenteId;

    @NotBlank
    @NotNull
    @Column(nullable = false)
    private String retroalimentacion;

    @NotBlank
    @NotNull
    @Column(nullable = false)
    private LocalDateTime fecha;
}
