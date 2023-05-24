package com.backend.softue.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"ideaNegocioId", "docenteId", "docentesApoyo", "estudiantesIntegrantes", "observaciones", "tutor", "estudianteLider"})
public class ObservacionIdea {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "Error: El campo 'ideaNegocioId' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la ideaNegocioId de la Observacion Idea.")
    @ManyToOne()
    @JoinColumn(name = "idea_negocio_Id")
    private IdeaNegocio ideaNegocioId;
    @Transient
    private String titulo;

    @NotNull(message = "Error: El campo 'docenteId' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el docenteId de la Observacion Idea.")
    @ManyToOne()
    @JoinColumn(name = "docente_id")
    private Docente docenteId;
    @Transient
    private String [][] docenteInfo;

    @NotBlank(message = "Error: El campo 'retroalimentacion' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para la retroalimentacion de la Observacion Idea.")
    @NotNull(message = "Error: El campo 'retroalimentacion' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la retroalimentacion de la Observacion Idea.")
    @Column(nullable = false)
    private String retroalimentacion;

    @NotNull(message = "Error: El campo 'fecha' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la fecha de la Observacion Idea.")
    @Column(nullable = false)
    private LocalDateTime fecha;
}
