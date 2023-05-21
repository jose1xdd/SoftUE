package com.backend.softue.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@NoArgsConstructor
@Getter
public class Roles {
    private final Set<String> nombreRoles = Set.of("estudiante", "coordinador", "administrativo", "docente");
    private final Map<String, Set<String>> permisosDeEdicion = Map.of("estudiante", Set.of(),
                                                             "coordinador", Set.of("estudiante", "coordinador", "docente"),
                                                             "administrativo", Set.of("estudiante", "coordinador", "administrativo", "docente"),
                                                             "docente", Set.of());
}
