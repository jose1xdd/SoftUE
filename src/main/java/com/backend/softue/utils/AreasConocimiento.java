package com.backend.softue.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Getter
@NoArgsConstructor
public class AreasConocimiento {
    private final Set<String> areasConocimiento = Set.of("minera", "agropecuaria", "comercial",
            "servicios", "industrial");
}
