package com.backend.softue.utils.beansAuxiliares;

import com.backend.softue.models.Docente;
import com.backend.softue.models.Estudiante;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
@Setter
@NoArgsConstructor
public class UsuariosValidos {

    private Map<String, Estudiante> estudianteMap = new HashMap<>();

    private Map<String, Docente> docenteMap = new HashMap<>();
}
