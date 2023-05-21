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
@EqualsAndHashCode
@Embeddable
public class ObservacionIdeaKey implements Serializable {

    @NotNull(message = "Error: El campo 'ideaNegocioId' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el ideaNegocioId de la llave compuesta de ObservacionIdea.")
    @Column(name = "idea_id")
    private Integer ideaNegocioId;

    @NotNull(message = "Error: El campo 'docenteId' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el docenteId de la llave compuesta de ObservacionIdea.")
    @Column(name = "docente_id")
    private Integer docenteId;
}
