package com.backend.softue.services;

import com.backend.softue.models.*;
import com.backend.softue.repositories.RespuestaSeleccionadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RespuestaSeleccionadaServices {

    @Autowired
    private RespuestaSeleccionadaRepository respuestaSeleccionadaRepository;

    public void crear(Test test, Respuesta respuesta) {
        this.respuestaSeleccionadaRepository.save(new RespuestaSeleccionada(new RespuestaSeleccionadaKey(respuesta.getId(), test.getId()), respuesta, test));
    }
}
