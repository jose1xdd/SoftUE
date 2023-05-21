package com.backend.softue.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Blob;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "Formato")
public class Formato {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "Error: El campo 'modulo' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el modulo del formato.")
    @Column(nullable = false)
    private String modulo;

    @NotNull(message = "Error: El campo 'fechaCreacion' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la fecha de creacion del formato.")
    @Column(nullable = false)
    private LocalDate fechaCreacion;

    @Lob
    @Column(nullable = false,columnDefinition = "LONGBLOB")
    private Blob documento;
}
