package com.backend.softue.services;

import com.backend.softue.models.CalificacionIdea;
import com.backend.softue.models.EvaluacionIdea;
import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.repositories.EvaluacionIdeaRepository;
import com.backend.softue.security.Hashing;
import com.backend.softue.utils.beansAuxiliares.EstadosCalificacion;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Setter
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

    private CalificacionIdeaServices calificacionIdeaServices;

    @Autowired
    private EstadosCalificacion estadosCalificacion;

    public void crearEvaluacion(String jwt, String titulo) {
        IdeaNegocio ideaNegocio = this.ideaNegocioServices.obtenerIdeaNegocio(titulo);
        if (ideaNegocio.getTutor() == null || !this.encrypt.getJwt().getKey(jwt).equals(ideaNegocio.getTutor().getCorreo()))
            throw new RuntimeException("Solo el tutor de una idea de negocio puede gestionar los docentes de apoyo de la misma");
        EvaluacionIdea evaluacionReciente = null;
        try {
            evaluacionReciente = this.obtenerEvaluacionReciente(titulo);
        }
        catch (Exception e) {
        }
        if(evaluacionReciente != null) {
            List<CalificacionIdea> calificaciones = this.calificacionIdeaServices.obtenerCalificaciones(evaluacionReciente);
            if(calificaciones == null || calificaciones.size() == 0)
                throw new RuntimeException("No se puede crear una evaluación a una idea con una evaluación pendiente");
            for(CalificacionIdea calificacion : calificaciones) {
                if(calificacion.getEstado().equals(this.estadosCalificacion.getEstados()[2]) || calificacion.getEstado().equals(this.estadosCalificacion.getEstados()[3]))
                    throw new RuntimeException("No se puede crear una evaluación a una idea con una evaluación pendiente");
            }
        }
        this.evaluacionIdeaRepository.save(new EvaluacionIdea(null, LocalDate.now(), LocalDate.now().plusDays(periodoServices.obtener().getPeriodoIdeaNegocio().getDays()), ideaNegocio, null));
    }

    public EvaluacionIdea obtenerEvaluacionReciente(String titulo) {
        IdeaNegocio ideaNegocio = this.ideaNegocioServices.obtenerIdeaNegocio(titulo);
        Optional<EvaluacionIdea> resultado = this.evaluacionIdeaRepository.evaluacionReciente(titulo);
        if(!resultado.isPresent())
            throw new RuntimeException("La idea de negocio no presenta ninguna evaluación.");
        return resultado.get();
    }

    public EvaluacionIdea obtener(Integer id) {
        Optional<EvaluacionIdea> resultado = this.evaluacionIdeaRepository.findById(id);
        if(!resultado.isPresent())
            throw new RuntimeException("La evaluación consultada no existe");
        return resultado.get();
    }
}
