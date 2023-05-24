package com.backend.softue.services;

import com.backend.softue.models.EvaluacionIdea;
import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.repositories.EvaluacionIdeaRepository;
import com.backend.softue.security.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EvaluacionIdeaServices {

    @Autowired
    private EvaluacionIdeaRepository evaluacionIdeaRepository;

    @Autowired
    private IdeaNegocioServices ideaNegocioServices;

    @Autowired
    private Hashing encrypt;

    @Autowired
    private PeriodoServices periodoServices;
    public void crearEvaluacion(String jwt, String titulo) {
        IdeaNegocio ideaNegocio = this.ideaNegocioServices.obtenerIdeaNegocio(titulo);
        if (ideaNegocio.getTutor() == null || !this.encrypt.getJwt().getKey(jwt).equals(ideaNegocio.getTutor().getCorreo()))
            throw new RuntimeException("Solo el tutor de una idea de negocio puede gestionar los docentes de apoyo de la misma");
        this.evaluacionIdeaRepository.save(new EvaluacionIdea(null, LocalDate.now(), LocalDate.now().plusDays(periodoServices.obtener().getDiasPeriodoIdea()), ideaNegocio, null));
    }
}
