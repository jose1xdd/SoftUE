package com.backend.softue.services;

import com.backend.softue.models.*;
import com.backend.softue.repositories.CalificacionIdeaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalificacionIdeaServices {

    @Autowired
    private CalificacionIdeaRepository calificacionIdeaRepository;

    @Autowired
    private  IdeaNegocioServices ideaNegocioServices;

    @Autowired
    private  DocenteServices docenteServices;

    @Autowired
    private EvaluacionIdeaServices evaluacionIdeaServices;

    @PostConstruct
    public void init() {
        this.evaluacionIdeaServices.setCalificacionIdeaServices(this);
    }

    public void crear(String titulo, String correo) {
        IdeaNegocio ideaNegocio = this.ideaNegocioServices.obtenerIdeaNegocio(titulo);
        Docente docente = this.docenteServices.obtenerDocente(correo);
        /*if(docente.getCorreo().equals(ideaNegocio.getTutorInfo()[0][0])
            throw new RuntimeException("El tutor de la idea de negocio a calificar no puede ser evaluador.");
        */
        EvaluacionIdea evaluacionIdea = this.evaluacionIdeaServices.obtenerEvaluacionReciente(ideaNegocio.getTitulo());
        //Hace falta cuadrar la fecha de las calificaciones
    }

    public List<CalificacionIdea> obtenerCalificaciones(EvaluacionIdea evaluacionIdea) {
        try {
            evaluacionIdea = this.evaluacionIdeaServices.obtener(evaluacionIdea.getId());
        }
        catch (Exception e) {
            throw new RuntimeException("No se pueden obtener calificaciones de una evaluación que no existe");
        }
        List<CalificacionIdea> calificaciones = this.calificacionIdeaRepository.findByEvaluacion(evaluacionIdea.getId());

        return calificaciones;
    }
}
