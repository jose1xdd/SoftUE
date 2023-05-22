package com.backend.softue.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@JsonIgnoreProperties({"documentoIdea", "evaluaciones", "docentesApoyo", "estudiantesIntegrantes", "observaciones", "tutor", "estudianteLider"})
@Table(name = "Idea_negocio")
public class IdeaNegocio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "Error: El campo 'titulo' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el titulo de la Idea de Negocio.")
    @NotNull(message = "Error: El campo 'titulo' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el titulo de la Idea de Negocio.")
    @Column(nullable = false, unique = true)
    private String titulo;

    @NotNull(message = "Error: El campo 'estado' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el estado de la Idea de Negocio.")
    @Column(nullable = false)
    private Character estado;

    @NotBlank(message = "Error: El campo 'areaEnfoque' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el area de enfoque de la Idea de Negocio.")
    @NotNull(message = "Error: El campo 'areaEnfoque' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el area de enfoque de la Idea de Negocio.")
    @Column(nullable = false)
    private String areaEnfoque;

    @ManyToOne
    @JoinColumn(name = "tutor_codigo")
    private Docente tutor;
    @Transient
    private String correoTutor;

    @NotNull(message = "Error: El campo 'fechaCreacion' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la fecha de creacion de la Idea de Negocio.")
    @Column(nullable = false)
    private LocalDate fechaCreacion;

    //@NotNull(message = "Error: El campo 'estudianteLider' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el estudiante lider de la Idea de Negocio.")
    @ManyToOne
    @JoinColumn(nullable = false, name = "codigo_estudiante_lider")
    private Estudiante estudianteLider;
    @Transient
    private String correoEstudianteLider;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_idea_id", referencedColumnName = "documento_idea_id")
    private DocumentoIdea documentoIdea;
    @Transient
    private Integer idDocumentoIdea;

    @OneToMany(mappedBy = "ideaNegocio", fetch = FetchType.LAZY)
    private Set<EvaluacionIdea> evaluaciones;

    @OneToMany(mappedBy = "ideaNegocio", fetch = FetchType.LAZY)
    private Set<DocenteApoyoIdea> docentesApoyo;


    @OneToMany(mappedBy = "ideaNegocio", fetch = FetchType.LAZY)
    private Set<IdeaPlanteada> estudiantesIntegrantes;
    @Transient
    private String [] correoEstudiantesIntegrantes;


    @OneToMany(mappedBy = "ideaNegocioId", fetch = FetchType.LAZY)
    private Set<ObservacionIdea> observaciones;
}
