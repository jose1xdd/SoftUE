package com.backend.softue.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
@Entity
public class ObservacionIdea  implements Serializable {
    @EmbeddedId
    ObservacionIdeaKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ideaNegocioId")
    @JoinColumn(name = "idea_negocio_Id")
    private IdeaNegocio ideaNegocioId;

    @ManyToOne(fetch = FetchType.LAZY)
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
