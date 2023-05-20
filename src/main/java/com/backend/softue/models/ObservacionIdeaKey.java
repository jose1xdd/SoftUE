package com.backend.softue.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class ObservacionIdeaKey {
    @Column(name = "idea_id")
    private Integer ideaNegocioId;

    @Column(name = "docente_id")
    private Integer docenteId;
}
