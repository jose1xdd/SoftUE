package com.backend.softue.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import jakarta.validation.constraints.Email;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "Usuario")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int codigo;

    @NotBlank
    @NotNull
    @Column(nullable = false)
    private String nombre;

    @NotBlank
    @NotNull
    @Column(nullable = false)
    private String apellido;

    @NotNull
    @Column(nullable = false)
    private LocalDate fecha_nacimiento;

    @NotBlank
    @NotNull
    @Column(nullable = false)
    private String sexo;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String correo;

    @NotBlank
    @NotNull
    @Column(nullable = false, unique = true)
    private String telefono;

    @NotBlank
    @NotNull
    @Column(nullable = false)
    private String contrasenia;

    @NotBlank
    @NotNull
    @Column(nullable = false)
    private String tipo_usuario;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "foto_usuario")
    private FotoUsuario foto_usuario;
}

