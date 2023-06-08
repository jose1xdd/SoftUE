package com.backend.softue.services;

import com.backend.softue.models.CalificacionPlan;
import com.backend.softue.models.EvaluacionIdea;
import com.backend.softue.models.EvaluacionPlan;
import com.backend.softue.models.PlanNegocio;
import com.backend.softue.repositories.EvaluacionPlanRepository;
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
public class EvaluacionPlanServices {

    @Autowired
    private EvaluacionPlanRepository evaluacionPlanRepository;

    private  PlanNegocioServices planNegocioServices;

    @Autowired
    private Hashing encrypt;

    private CalificacionPlanServices calificacionPlanServices;

    @Autowired
    private EstadosCalificacion estadosCalificacion;

    @Autowired
    private PeriodoServices periodoServices;

    public void crearEvaluacion(String jwt, String titulo) {
        PlanNegocio planNegocio = this.planNegocioServices.obtenerPlanNegocio(titulo);
        if(planNegocio.getTutor() == null || !this.encrypt.getJwt().getKey(jwt).equals(planNegocio.getTutor().getCorreo()))
            throw new RuntimeException("Solo el tutor de un plan de negocio puede enviarla a evaluar");
        EvaluacionPlan evaluacionReciente = null;
        try {
            evaluacionReciente = this.obtenerEvaluacionReciente(titulo);
        }
        catch (Exception e) {
        }
        if(evaluacionReciente != null) {
            List<CalificacionPlan> calificaciones = this.calificacionPlanServices.obtenerCalificacionesDeEvaluacion(evaluacionReciente);
            if (planNegocio.getEstado().equals(this.estadosCalificacion.getEstados()[2]) ||
                    planNegocio.getEstado().equals(this.estadosCalificacion.getEstados()[3]))
                throw new RuntimeException("No se puede crear una nueva evaluación si existe una que se encuentre pendiente por calificaciones");
            boolean pendiente = false;
            for (CalificacionPlan calificacionPlan : calificaciones) {
                pendiente |= calificacionPlan.getEstado().equals(this.estadosCalificacion.getEstados()[2]);
            }
            if (pendiente)
                throw new RuntimeException("No se puede crear una nueva evaluación si presenta una calificación pendiente por nota.");
        }
        if(planNegocio.getEstado().equals(this.estadosCalificacion.getEstados()[0]))
            throw new RuntimeException("No se puede crear una evaluación nueva a un plan de negocio ya aprobado.");
        this.planNegocioServices.actualizarEstado(planNegocio.getTitulo(), this.estadosCalificacion.getEstados()[2]);
        this.evaluacionPlanRepository.save(new EvaluacionPlan(null, LocalDate.now(), LocalDate.now().plusDays(this.periodoServices.obtener().getPeriodoPlanNegocio().getDays()), planNegocio, null, null));
    }

    public EvaluacionPlan obtenerEvaluacionReciente(String titulo) {
        PlanNegocio planNegocio = this.planNegocioServices.obtenerPlanNegocio(titulo);
        return this.evaluacionReciente(planNegocio);
    }

    public EvaluacionPlan obtenerEvaluacionReciente(PlanNegocio planNegocio) {
        if(planNegocio == null)
            throw new RuntimeException("Información insuficiente para obtener la evaluación más reciente de un plan de negocio");
        return this.evaluacionReciente(planNegocio);
    }

    private EvaluacionPlan evaluacionReciente(PlanNegocio planNegocio) {
        Optional<EvaluacionPlan> resultado = this.evaluacionPlanRepository.evaluacionReciente(planNegocio.getTitulo());
        if(!resultado.isPresent())
            throw new RuntimeException("El plan de negocio no presenta ninguna evaluación.");
        EvaluacionPlan evaluacion = resultado.get();
        evaluacion.setCalificacionesInfo(this.calificacionPlanServices.obtenerCalificacionesDeEvaluacion(evaluacion));
        return evaluacion;
    }

    public EvaluacionPlan obtener(Integer id) {
        Optional<EvaluacionPlan> resultado = this.evaluacionPlanRepository.findById(id);
        if(!resultado.isPresent())
            throw new RuntimeException("La evaluación consultada no existe");
        EvaluacionPlan evaluacionPlan = resultado.get();
        return evaluacionPlan;
    }

    public List<EvaluacionPlan> listar (String titulo, String JWT) {
        PlanNegocio planNegocio = this.planNegocioServices.obtenerPlanNegocio(titulo);
        List<EvaluacionPlan> evaluaciones = this.evaluacionPlanRepository.findByPlanNegocio(planNegocio.getId());
        boolean esEvaluador = false;
        for(int i = 0; i < evaluaciones.size(); i++) {
            EvaluacionPlan evaluacionPlan = evaluaciones.get(i);
            evaluacionPlan.setCalificacionesInfo(this.calificacionPlanServices.obtenerCalificacionesDeEvaluacion(evaluacionPlan));
            evaluaciones.set(i, evaluacionPlan);
            esEvaluador |= this.esEvaluador(evaluacionPlan, JWT);
        }
        if(!(esEvaluador || this.permisos(planNegocio, JWT)))
            throw new RuntimeException("El usuario no presenta permisos sufientes para visualizar las evaluaciones del plan de negocio.");
        return evaluaciones;
    }

    public void actualizar(EvaluacionPlan evaluacionPlan) {
        EvaluacionPlan resultado = this.obtener(evaluacionPlan.getId());
        this.evaluacionPlanRepository.save(evaluacionPlan);
    }

    private boolean permisos(PlanNegocio planNegocio, String JWT) {
        String correo = this.encrypt.getJwt().getKey(JWT), rol = this.encrypt.getJwt().getValue(JWT);
        if(rol.equals("coordinador") || rol.equals("administrativo"))
            return true;
        if(correo.equals(planNegocio.getEstudianteLiderInfo()[0][0]))
            return true;
        if(correo.equals(planNegocio.getTutorInfo()[0][0]))
            return true;
        for(String correoIntegrantes : planNegocio.getEstudiantesIntegrantesInfo()[0]) {
            if(correoIntegrantes.equals(correo))
                return true;
        }
        for(String correoDocentesApoyo : planNegocio.getDocentesApoyoInfo()[0]) {
            if(correoDocentesApoyo.equals(correo))
                return true;
        }
        return false;
    }

    private boolean esEvaluador(EvaluacionPlan evaluacionPlan, String JWT) {
        String correo = this.encrypt.getJwt().getKey(JWT);
        for(CalificacionPlan calificacionPlan : evaluacionPlan.getCalificacionesInfo()) {
            if(calificacionPlan.getDocente().getCorreo().equals(correo))
                return true;
        }
        return false;
    }
}
