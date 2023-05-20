package com.backend.softue.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "Entidad_financiadora")
public class EntidadFinanciadora {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank
    @NotNull
    @Column(nullable = false)
    private String nombre;

    @NotBlank
    @NotNull
    @Column(nullable = false)
    private String telefono;

    @NotBlank
    @NotNull
    @Column(nullable = false)
    private String sitioWeb;

    @Email
    @NotBlank
    @Column(nullable = false)
    private String correo;

    @Email
    @NotBlank
    @Column(nullable = false)
    private String descripcion;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "foto_entidad_financiadora_id", referencedColumnName = "id")
    private FotoEntidadFinanciadora fotoEntidadFinanciadoraId;
}
