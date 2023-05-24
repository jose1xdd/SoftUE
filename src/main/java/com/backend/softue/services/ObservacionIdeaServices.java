package com.backend.softue.services;

import com.backend.softue.models.Docente;
import com.backend.softue.models.DocenteApoyoIdea;
import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.models.ObservacionIdea;
import com.backend.softue.repositories.ObservacionIdeaRepository;
import com.backend.softue.security.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class ObservacionIdeaServices {

    @Autowired
    private DocenteServices docenteServices;
    @Autowired
    private IdeaNegocioServices ideaNegocioServices;

    @Autowired
    private DocenteApoyoIdeaServices docenteApoyoIdeaServices;

    @Autowired
    private ObservacionIdeaRepository observacionIdeaRepository;

    @Autowired
    private Hashing encrypt;
    public void crearObservacion(String jwt, String ideaTitulo, String observacion){
        String correoDocente = this.encrypt.getJwt().getKey(jwt);
        if(ideaTitulo == null)
            throw new RuntimeException("No se suministro un titulo de la idea de negocio");
        if(observacion == null)
            throw new RuntimeException("No se suministro una observacion");

        IdeaNegocio idea = this.ideaNegocioServices.obtenerIdeaNegocio(ideaTitulo);
        if(idea == null)
            throw new RuntimeException("No existe una idea de negocio asignada a ese titulo");

        Docente docente = this.docenteServices.obtenerDocente(correoDocente);
        if(docente == null)
            throw new RuntimeException("No existe un docente asignado a ese correo");

        List<DocenteApoyoIdea> docentes = this.docenteApoyoIdeaServices.listarDocentesApoyo(idea);
        boolean esDocente = false;
        for(DocenteApoyoIdea v : docentes){
            if(v.getDocente().getCorreo().equals(correoDocente)) esDocente = true;
        }

        if(!esDocente && !idea.getTutor().getCorreo().equals(correoDocente))
            throw new RuntimeException("El docente no tiene permisos para realizar la observacion");
        this.observacionIdeaRepository.save(new ObservacionIdea(null, idea, docente, observacion, LocalDateTime.now()));

    }
}
