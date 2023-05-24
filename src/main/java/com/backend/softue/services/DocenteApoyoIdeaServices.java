package com.backend.softue.services;

import com.backend.softue.models.Docente;
import com.backend.softue.models.DocenteApoyoIdea;
import com.backend.softue.models.DocenteIdeaKey;
import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.repositories.DocenteApoyoIdeaRepository;
import com.backend.softue.security.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocenteApoyoIdeaServices {

    @Autowired
    private DocenteApoyoIdeaRepository docenteApoyoIdeaRepository;

    @Autowired
    private IdeaNegocioServices ideaNegocioServices;

    @Autowired
    private DocenteServices docenteServices;

    @Autowired
    private Hashing encrypt;
    public void agregarDocenteApoyo(String jwt, String tituloIdea, String correoDocente) {
        if (tituloIdea == null)
            throw new RuntimeException("No se envió un titulo con el que buscar la idea de negocio");
        if (correoDocente == null)
            throw new RuntimeException("No se envió un correo con el que buscar al docente");
        IdeaNegocio ideaNegocio = this.ideaNegocioServices.obtenerIdeaNegocio(tituloIdea);
        if (ideaNegocio.getTutor() == null || !this.encrypt.getJwt().getKey(jwt).equals(ideaNegocio.getTutor().getCorreo()))
            throw new RuntimeException("Solo el tutor de una idea de negocio puede gestionar los docentes de apoyo de la misma");
        Docente docente = this.docenteServices.obtenerDocente(correoDocente);
        this.docenteApoyoIdeaRepository.save(new DocenteApoyoIdea(new DocenteIdeaKey(docente.getCodigo(), ideaNegocio.getId()), docente, ideaNegocio));
    }

    public void eliminarDocenteApoyo(String jwt, String tituloIdea, String correoDocente) {
        if (tituloIdea == null)
            throw new RuntimeException("No se envió un titulo con el que buscar la idea de negocio");
        if (correoDocente == null)
            throw new RuntimeException("No se envió un correo con el que buscar al docente");
        IdeaNegocio ideaNegocio = this.ideaNegocioServices.obtenerIdeaNegocio(tituloIdea);
        if (!this.encrypt.getJwt().getKey(jwt).equals(ideaNegocio.getTutor().getCorreo()))
            throw new RuntimeException("Solo el tutor de una idea de negocio puede gestionar los docentes de apoyo de la misma");
        Docente docente = this.docenteServices.obtenerDocente(correoDocente);
        DocenteApoyoIdea docenteApoyoIdea = this.docenteApoyoIdeaRepository.getReferenceById(new DocenteIdeaKey(docente.getCodigo(), ideaNegocio.getId()));
        this.docenteApoyoIdeaRepository.delete(docenteApoyoIdea);
    }
}
