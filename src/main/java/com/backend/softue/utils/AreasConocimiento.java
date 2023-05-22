package com.backend.softue.utils;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Getter
public class AreasConocimiento {
    private final Set<String> areasConocimiento = Set.of("minera", "agropecuaria", "comercial",
            "servicios", "Industrial");
}
