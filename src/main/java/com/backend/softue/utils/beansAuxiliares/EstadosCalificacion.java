package com.backend.softue.utils.beansAuxiliares;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Getter
@NoArgsConstructor
public class EstadosCalificacion {
    private final String[] estados = {"aprobada", "rechazada", "pendiente", "vencida"};
}
