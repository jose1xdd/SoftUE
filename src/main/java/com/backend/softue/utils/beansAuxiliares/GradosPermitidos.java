package com.backend.softue.utils.beansAuxiliares;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Getter
@NoArgsConstructor
public class GradosPermitidos {
    private final Set<String> grados = Set.of("primero", "segundo", "tercero", "cuarto", "quinto", "sexto", "septimo", "octavo",
            "noveno", "decimo", "once");
}
