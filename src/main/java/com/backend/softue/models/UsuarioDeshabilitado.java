package com.backend.softue.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "usuario_desahabilitado")
public class UsuarioDeshabilitado {
    @Id
    private Integer codigo;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private LocalDate fecha_nacimiento;

    @Column(nullable = false)
    private String sexo;

    @Column(nullable = false)
    private String correo;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String tipo_usuario;

    @Column(nullable = true)
    private String curso;

    @Column(nullable = true)
    private String nombreAcudiente;

    @Column(nullable = true)
    private String capacitacionAprobada;

    @Column(nullable = true)
    private String cedula;

    @Column(nullable = true)
    private String titulo;

    @Column(nullable = true)
    private String area;

    public UsuarioDeshabilitado(User user) {
        this.codigo = user.getCodigo();
        this.nombre = user.getNombre();
        this.apellido = user.getApellido();
        this.fecha_nacimiento = user.getFecha_nacimiento();
        this.sexo = user.getSexo();
        this.correo = user.getCorreo();
        this.telefono = user.getTelefono();
        this.tipo_usuario = user.getTipo_usuario();
    }

    public UsuarioDeshabilitado(Estudiante estudiante) {
        this.codigo = estudiante.getCodigo();
        this.nombre = estudiante.getNombre();
        this.apellido = estudiante.getApellido();
        this.fecha_nacimiento = estudiante.getFecha_nacimiento();
        this.sexo = estudiante.getSexo();
        this.correo = estudiante.getCorreo();
        this.telefono = estudiante.getTelefono();
        this.tipo_usuario = estudiante.getTipo_usuario();
        this.curso = estudiante.getCurso();
        this.nombreAcudiente = estudiante.getNombreAcudiente();
        this.capacitacionAprobada = estudiante.getCapacitacionAprobada();
    }

    public UsuarioDeshabilitado(Docente docente) {
        this.codigo = docente.getCodigo();
        this.nombre = docente.getNombre();
        this.apellido = docente.getApellido();
        this.fecha_nacimiento = docente.getFecha_nacimiento();
        this.sexo = docente.getSexo();
        this.correo = docente.getCorreo();
        this.telefono = docente.getTelefono();
        this.tipo_usuario = docente.getTipo_usuario();
        this.cedula = docente.getCedula();
        this.titulo = docente.getTitulo();
        this.area = docente.getArea();
    }
}
