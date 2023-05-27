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
            throw new RuntimeException("Solo el tutor de una idea de negocio puede enviarla a evaluar");
        EvaluacionIdea evaluacionReciente = null;
        try {
            evaluacionReciente = this.obtenerEvaluacionReciente(titulo);
        }
        catch (Exception e) {
        }
        if(evaluacionReciente != null) {
            List<CalificacionIdea> calificaciones = this.calificacionIdeaServices.obtenerCalificacionesDeEvaluacion(evaluacionReciente);
            if(calificaciones == null || calificaciones.size() == 0)
                throw new RuntimeException("No se puede crear una evaluación a una idea con una evaluación pendiente");
            for(CalificacionIdea calificacion : calificaciones) {
                if(calificacion.getEstado().equals(this.estadosCalificacion.getEstados()[2]) || calificacion.getEstado().equals(this.estadosCalificacion.getEstados()[3]))
                    throw new RuntimeException("No se puede crear una evaluación a una idea con una evaluación pendiente");
            }
        }
        if(ideaNegocio.getEstado().equals("aprobada"))
            throw new RuntimeException("No se puede crear una evaluación nueva a una idea de negocio ya aprobada.");
        this.ideaNegocioServices.actualizarEstado(ideaNegocio.getTitulo(), "pendiente");
        this.evaluacionIdeaRepository.save(new EvaluacionIdea(null, LocalDate.now(), LocalDate.now().plusDays(periodoServices.obtener().getPeriodoIdeaNegocio().getDays()), ideaNegocio, null, null));
    }

    public EvaluacionIdea obtenerEvaluacionReciente(String titulo) {
        IdeaNegocio ideaNegocio = this.ideaNegocioServices.obtenerIdeaNegocio(titulo);
        return evaluacionReciente(ideaNegocio);
    }

    public EvaluacionIdea obtenerEvaluacionReciente(IdeaNegocio ideaNegocio) {
        if(ideaNegocio == null)
            throw new RuntimeException("Información insuficiente para obtener la evaluación más reciente de una idea de negocio");
        return evaluacionReciente(ideaNegocio);
    }

    private EvaluacionIdea evaluacionReciente(IdeaNegocio ideaNegocio) {
        Optional<EvaluacionIdea> resultado = this.evaluacionIdeaRepository.evaluacionReciente(ideaNegocio.getTitulo());
        if(!resultado.isPresent())
            throw new RuntimeException("La idea de negocio no presenta ninguna evaluación.");
        EvaluacionIdea evaluacion = resultado.get();
        evaluacion.setCalificacionesInfo(this.calificacionIdeaServices.obtenerCalificacionesDeEvaluacion(evaluacion));
        return evaluacion;
    }

    public EvaluacionIdea obtener(Integer id) {
        Optional<EvaluacionIdea> resultado = this.evaluacionIdeaRepository.findById(id);
        if(!resultado.isPresent())
            throw new RuntimeException("La evaluación consultada no existe");
        EvaluacionIdea evaluacionIdea = resultado.get();
        return evaluacionIdea;
    }

    public List<EvaluacionIdea> listar (String titulo, String JWT){
        IdeaNegocio ideaNegocio = this.ideaNegocioServices.obtenerIdeaNegocio(titulo);
        List<EvaluacionIdea> evaluaciones = this.evaluacionIdeaRepository.findByIdeaNegocio(ideaNegocio.getId());
        boolean esEvaluador = false;
        for(int i = 0; i < evaluaciones.size(); i++) {
            EvaluacionIdea evaluacionIdea = evaluaciones.get(i);
            evaluacionIdea.setCalificacionesInfo(this.calificacionIdeaServices.obtenerCalificacionesDeEvaluacion(evaluacionIdea));
            evaluaciones.set(i, evaluacionIdea);
            esEvaluador |= esEvaluador(evaluacionIdea, JWT);
        }
        if(!(esEvaluador || permisos(ideaNegocio, JWT)))
            throw new RuntimeException("El usuario no presenta permisos sufientes para visualizar las evaluaciones de la idea de negocio.");
        return evaluaciones;
    }

    public void actualizar(EvaluacionIdea evaluacionIdea) {
        EvaluacionIdea resultado = this.obtener(evaluacionIdea.getId());
        this.evaluacionIdeaRepository.save(evaluacionIdea);
    }

    private boolean permisos(IdeaNegocio ideaNegocio, String JWT) {
        String correo = this.encrypt.getJwt().getKey(JWT), rol = this.encrypt.getJwt().getValue(JWT);
        if(rol.equals("coordinador") || rol.equals("administrativo"))
            return true;
        if(correo.equals(ideaNegocio.getEstudianteLiderInfo()[0][0]))
            return true;
        if(correo.equals(ideaNegocio.getTutorInfo()[0][0]))
            return true;
        for(String correoIntegrantes : ideaNegocio.getEstudiantesIntegrantesInfo()[0]) {
            if(correoIntegrantes.equals(correo))
                return true;
        }
        for(String correoDocentesApoyo : ideaNegocio.getDocentesApoyoInfo()[0]) {
            if(correoDocentesApoyo.equals(correo))
                return true;
        }
        return false;
    }

    private boolean esEvaluador(EvaluacionIdea evaluacionIdea, String JWT) {
        String correo = this.encrypt.getJwt().getKey(JWT);
        for(CalificacionIdea calificacionIdea : evaluacionIdea.getCalificacionesInfo()) {
            if(calificacionIdea.getDocente().getCorreo().equals(correo))
                return true;
        }
        return false;
    }
}
