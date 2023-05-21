package com.backend.softue.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Embeddable
public class DocentePlanKey implements Serializable {

    @NotNull(message = "Error: El campo 'codigoDocente' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el codigoDocente de la llave compuesta de los docentes de apoyo del plan de negocio (DocentePlanKey).")
    @Column(nullable = false, name = "docente_codigo")
    private Integer codigoDocente;

    @NotNull(message = "Error: El campo 'planNegocio' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el planNegocio de la llave compuesta de los docentes de apoyo del plan de negocio (DocentePlanKey).")
    @Column(nullable = false, name = "planNegocio_id")
    private Integer planNegocio;
}
