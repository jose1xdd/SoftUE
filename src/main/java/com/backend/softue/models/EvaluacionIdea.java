package com.backend.softue.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "Evaluacion_idea")
@JsonIgnoreProperties({"calificaciones", "ideaNegocio"})
public class EvaluacionIdea {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "Error: El campo 'fechaPresentacion' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la fecha de presentacion de la Evaluacion Idea.")
    @Column(nullable = false, name = "fecha_presentacion")
    private LocalDate fechaPresentacion;

    @NotNull(message = "Error: El campo 'fechaCorte' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la fecha de corte de la Evaluacion Idea.")
    @Column(nullable = false, name = "fecha_corte")
    private LocalDate fechaCorte;

    @NotNull(message = "Error: El campo 'ideaNegocio' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la idea de negocio de la Evaluacion Idea.")
    @ManyToOne
    @JoinColumn(name = "idea_negocio", nullable = false)
    private IdeaNegocio ideaNegocio;

    @OneToMany(mappedBy = "evaluacionIdeaId", fetch = FetchType.LAZY)
    private Set<CalificacionIdea> calificaciones;
    @Transient
    private List<CalificacionIdea> calificacionesInfo;
}
