package com.backend.softue.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "Estudiante")
public class Estudiante extends User{

    @NotBlank
    @NotNull
    @Column(nullable = false)
    private String curso;

    @NotBlank
    @NotNull
    @Column(nullable = false, name = "nombre_acudiente")
    private String nombreAcudiente;

    @NotBlank
    @NotNull
    @Column(nullable = false, name = "capacitacion_aprobada")
    private String capacitacionAprobada;

}
