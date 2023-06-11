package com.backend.softue.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class RespuestaSeleccionadaKey {

    @NotNull()
    @Column(name = "respuesta_id")
    private Integer respuestaId;

    @NotNull()
    @Column(name = "test_id")
    private Integer testId;
}
