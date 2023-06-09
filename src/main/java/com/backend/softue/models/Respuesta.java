package com.backend.softue.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "Error: El campo 'contenido' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el contenido de la Respuesta.")
    @Column(nullable = false)
    private String contenido;

    @NotNull(message = "Error: El campo 'valor' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la Respuesta.")
    @Column(nullable = false)
    private Integer valor;

    @NotNull(message = "Error: El campo 'pregunta_id' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el pregunta_id de la respuesta.")
    @ManyToOne()
    @JoinColumn(name = "pregunta_id")
    private Pregunta preguntaId;
}
