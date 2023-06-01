package com.backend.softue.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
@JsonIgnoreProperties({"foto_usuario"})
@Table(name = "Usuario")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer codigo;

    @NotBlank(message = "Error: El campo 'nombre' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el nombre del usuario.")
    @NotNull(message = "Error: El campo 'nombre' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el nombre del usuario.")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "Error: El campo 'apellido' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el apellido del usuario.")
    @NotNull(message = "Error: El campo 'apellido' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el apellido del usuario.")
    @Column(nullable = false)
    private String apellido;

    @NotNull(message = "Error: El campo 'fecha_nacimiento' no puede ser nulo. Por favor, asegúrese de proporcionar un valor valido para la fecha de nacimiento del usuario.")
    @Column(nullable = false)
    private LocalDate fecha_nacimiento;

    @NotBlank(message = "Error: El campo 'sexo' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el sexo del usuario.")
    @NotNull(message = "Error: El campo 'sexo' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el sexo del usuario.")
    @Column(nullable = false)
    private String sexo;


    @Email(message = "Error: El campo 'email' no es valido. Asegúrese de proporcionar una direccion de correo electronico valida para el usuario.")
    @NotBlank(message = "Error: El campo 'email' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el email del usuario.")
    @NotNull(message = "Error: El campo 'email' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el email del usuario.")
    @Column(nullable = false, unique = true)
    private String correo;

    @NotBlank(message = "Error: El campo 'telefono' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el telefono del usuario.")
    @NotNull(message = "Error: El campo 'telefono' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el telefono del usuario.")
    @Column(nullable = false, unique = true)
    private String telefono;

    @NotBlank(message = "Error: El campo 'contrasenia' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para la contrasenia del usuario.")
    @NotNull(message = "Error: El campo 'contrasenia' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la contrasenia del usuario.")
    @Column(nullable = false)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&+]).{6,}$",message = "Error: La contraseña que ha ingresado debe cumplir con los siguientes requisitos minimos : almenos 6 caracteres ,una letra en mayuscula, un numero y un caracter especial")
    private String contrasenia;

    @NotBlank(message = "Error: El campo 'tipo_usuario' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el tipo del usuario.")
    @NotNull(message = "Error: El campo 'tipo_usuario' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el tipo del usuario.")
    @Column(name = "tipo_usuario",nullable = false)
    private String tipoUsuario;

    @OneToOne(mappedBy = "usuarioCodigo",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private FotoUsuario foto_usuario;
    @Transient
    private Integer fotoUsuarioId;
}

