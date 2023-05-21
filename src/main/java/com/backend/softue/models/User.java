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
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Usuario")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer codigo;

    @NotBlank(message = "")
    @NotNull(message = "")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "")
    @NotNull(message = "")
    @Column(nullable = false)
    private String apellido;

    @NotNull(message = "")
    @Column(nullable = false)
    private LocalDate fecha_nacimiento;

    @NotBlank(message = "")
    @NotNull(message = "")
    @Column(nullable = false)
    private String sexo;

    @Email(message = "Email no valido")
    @NotBlank(message = "")
    @Column(nullable = false, unique = true)
    private String correo;

    @NotBlank(message = "")
    @NotNull(message = "")
    @Column(nullable = false, unique = true)
    private String telefono;

    @NotBlank(message = "")
    @NotNull(message = "")
    @Column(nullable = false)
    private String contrasenia;

    @NotBlank(message = "")
    @NotNull(message = "")
    @Column(nullable = false)
    private String tipo_usuario;

    @OneToOne(mappedBy = "usuarioCodigo",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private FotoUsuario foto_usuario;
}

