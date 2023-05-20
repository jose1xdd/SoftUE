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
    @Column(nullable = false, name = "docente_codigo")
    //private Docente docente;
    //@Transient
    private Integer codigoDocente;

    @NotNull
    @Column(nullable = false)
    private LocalDate fechaCreacion;

    @NotNull
    @Column(nullable = false, name = "estudiante_codigo")
    //private Estudiante estudiante;
    //@Transient
    private Integer codigoEstudiante;

    @NotNull
    @Column(nullable = false, name = "documento_idea_ID")
    private Integer IdDocumentoIdea;

    @OneToMany(mappedBy = "ideaNegocio", fetch = FetchType.LAZY)
    private Set<EvaluacionIdea> evaluaciones;
}
