package com.backend.softue.models;

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
@Table(name = "Idea_negocio")
public class IdeaNegocio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank
    @NotNull
    @Column(nullable = false)
    private String titulo;

    @NotNull
    @Column(nullable = false)
    private Character estado;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String areaEnfoque;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false, name = "tutor_codigo")
    private Docente tutor;

    @NotNull
    @Column(nullable = false)
    private LocalDate fechaCreacion;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false, name = "codigo_estudiante_lider")
    private Estudiante estudianteLider;

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

    @OneToMany(mappedBy = "ideaNegocioId", fetch = FetchType.LAZY)
    private Set<ObservacionIdea> observaciones;
}
