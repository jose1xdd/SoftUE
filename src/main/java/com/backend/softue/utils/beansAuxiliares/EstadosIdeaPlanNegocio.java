package com.backend.softue.utils.beansAuxiliares;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Getter
@NoArgsConstructor
public class EstadosIdeaPlanNegocio {
    private final Set<String> estados = Set.of("aprobada", "rechazada", "formulada", "pendiente");
}
