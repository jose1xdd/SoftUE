package com.backend.softue.models;

import jakarta.persistence.*;
import lombok.*;
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
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellido;
    @Column(nullable = false)
    private LocalDate fecha_nacimiento;
    @Column(nullable = false)
    private char sexo;
    @Column(nullable = false)
    private String correo;
    @Column(nullable = false)
    private String telefono;
    @Column(nullable = false)
    private String contrasenia;
    @Column(nullable = false)
    private String tipo_usuario;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "foto_usuario")
    private FotoUsuario foto_usuario;
}
