package com.backend.softue.services;

import com.backend.softue.models.*;
import com.backend.softue.repositories.CalificacionIdeaRepository;
import com.backend.softue.utils.beansAuxiliares.EstadosCalificacion;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private EstadosCalificacion estadosCalificacion;

    @PostConstruct
    public void init() {
        this.evaluacionIdeaServices.setCalificacionIdeaServices(this);
    }

    public void crear(String titulo, String correo) {
        IdeaNegocio ideaNegocio = this.ideaNegocioServices.obtenerIdeaNegocio(titulo);
        Docente docente = this.docenteServices.obtenerDocente(correo);
        if(docente.getCorreo().equals(ideaNegocio.getTutorInfo()[0][0]))
            throw new RuntimeException("El tutor de la idea de negocio no puede ser evaluador.");

        for(String docenteApoyo : ideaNegocio.getDocentesApoyoInfo()[0]) {
            if(docente.getCorreo().equals(docenteApoyo))
                throw new RuntimeException("Un docente de apoyo de la idea de negocio no puede ser evaluador.");
        }

        EvaluacionIdea evaluacionIdea = null;
        try {
            evaluacionIdea = this.evaluacionIdeaServices.obtenerEvaluacionReciente(ideaNegocio.getTitulo());
        }
        catch (Exception e) {
            throw new RuntimeException("No se puede crear una calificación si no existe una evaluación pendiente");
        }

        if(this.obtenerCalificaciones(evaluacionIdea).size() >= 3)
            throw new RuntimeException("No se puede asignar más de 3 evaluadores a una evaluación.");
        this.calificacionIdeaRepository.save(new CalificacionIdea(new CalificacionIdeaKey(docente.getCodigo(), evaluacionIdea.getId()), docente, evaluacionIdea, this.estadosCalificacion.getEstados()[2], null, LocalDate.now(), evaluacionIdea.getFechaCorte()));
    }

    public List<CalificacionIdea> obtenerCalificaciones(EvaluacionIdea evaluacionIdea) {
        try {
            if(evaluacionIdea == null)
                throw new RuntimeException();
            evaluacionIdea = this.evaluacionIdeaServices.obtener(evaluacionIdea.getId());
        }
        catch (Exception e) {
            throw new RuntimeException("No se pueden obtener calificaciones de una evaluación que no existe");
        }
        List<CalificacionIdea> calificaciones = this.calificacionIdeaRepository.findByEvaluacion(evaluacionIdea.getId());
        for(CalificacionIdea calificacion : calificaciones) {
            if(LocalDate.now().isAfter(calificacion.getFechaCorte()) && !calificacion.getEstado().equals(this.estadosCalificacion.getEstados()[4])) {
                //La calificación se establece como vencida si aun no lo es
                calificacion.setEstado(this.estadosCalificacion.getEstados()[4]);
                this.actualizar(calificacion);
            }
        }
        return calificaciones;
    }

    public void actualizar(CalificacionIdea calificacionIdea) {
        if(calificacionIdea == null || calificacionIdea.getId() == null)
            throw new RuntimeException("Información incompleta para actualizar una calificación.");
        Optional<CalificacionIdea> resultado = this.calificacionIdeaRepository.findById(calificacionIdea.getId());
        if(!resultado.isPresent())
            throw new RuntimeException("No se puede actualizar una calificación que no existe");
        this.calificacionIdeaRepository.save(calificacionIdea);
    }
}
