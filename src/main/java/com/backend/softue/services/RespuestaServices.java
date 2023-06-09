package com.backend.softue.services;

import com.backend.softue.models.Pregunta;
import com.backend.softue.models.Respuesta;
import com.backend.softue.repositories.RespuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RespuestaServices {

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private PreguntaServices preguntaServices;

    public void crear(String contenido, Integer valor, Integer preguntaId) {
        if (contenido == null || contenido.equals(""))
            throw new RuntimeException("El contenido no puede estar vacío");
        if (valor == null)
            throw new RuntimeException("El valor de la respuesta no puede ser nulo");
        Pregunta pregunta = this.preguntaServices.obtener(preguntaId);
        this.respuestaRepository.save(new Respuesta(null, contenido, valor, pregunta));
    }
}
