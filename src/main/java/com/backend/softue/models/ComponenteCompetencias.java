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
@Table(name = "Componente_competencias")
public class ComponenteCompetencias {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "Error: El campo 'nombre' no puede ser vacío. Por favor, asegurese de proporcionar un valor valido para el nombre del componente.")
    @Column(nullable = false, unique = true)
    private String nombre;

    @NotNull(message = "Error: El campo 'valor_porcentaje' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el porcentaje del componente.")
    @Column(nullable = false)
    private Double valorPorcentaje;
}
