package com.backend.softue.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RespuestaSeleccionada {

    @EmbeddedId
    private RespuestaSeleccionadaKey id;

    @ManyToOne
    @MapsId("respuestaId")
    @JoinColumn(name = "respuesta_id")
    private Respuesta respuesta;

    @ManyToOne
    @MapsId("testId")
    @JoinColumn(name = "test_id")
    private Test test;
}
