package com.backend.softue.models;

import java.time.LocalDate;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Usuario")
public class usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int codigo;
    private String nombre;
    private String apellido;
    private LocalDate fecha_nacimiento;
    private char sexo;
    private String correo;
    private String telefono;
    private String contrasenia;
    private char estado;
    private String tipo_usuario;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "foto_usuario")
    private fotoUsuario foto_usuario;
}
