package com.backend.softue.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RespuestaSeleccionadaKey {

    @NotNull()
    @Column(name = "respuesta_id")
    private Integer respuestaId;

    @NotNull()
    @Column(name = "test_id")
    private Integer testId;
}
