package com.backend.softue.services;

import com.backend.softue.models.*;
import com.backend.softue.repositories.CalificacionPlanRepository;
import com.backend.softue.security.Hashing;
import com.backend.softue.utils.beansAuxiliares.EstadosCalificacion;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CalificacionPlanServices {

    @Autowired
    private EvaluacionPlanServices evaluacionPlanServices;

    @Autowired
    private CalificacionPlanRepository calificacionPlanRepository;

    @Autowired
    private DocenteServices docenteServices;

    @Autowired
    private EstadosCalificacion estadosCalificacion;

    @Autowired
    private Hashing encrypt;

    @Autowired
    private PlanNegocioServices planNegocioServices;

    @PostConstruct
    public void init() {
        this.evaluacionPlanServices.setCalificacionPlanServices(this);
    }

    public void crear(String titulo, String correo, LocalDate fechaCorte) {
        PlanNegocio planNegocio = this.planNegocioServices.obtenerPlanNegocio(titulo);
        Docente docente = this.docenteServices.obtenerDocente(correo);
        if(docente.getCorreo().equals(planNegocio.getTutorInfo()[0][0]))
            throw new RuntimeException("El tutor del plan de negocio no puede ser evaluador.");

        for(String docenteApoyo : planNegocio.getDocentesApoyoInfo()[0]) {
            if(docente.getCorreo().equals(docenteApoyo))
                throw new RuntimeException("Un docente de apoyo del plan de negocio no puede ser evaluador.");
        }

        EvaluacionPlan evaluacionPlan = null;
        try {
            evaluacionPlan = this.evaluacionPlanServices.obtenerEvaluacionReciente(planNegocio.getTitulo());
        }
        catch (Exception e) {
            throw new RuntimeException("No se puede crear una calificación si no existe una evaluación pendiente.");
        }

        if(this.obtenerCalificacionesDeEvaluacion(evaluacionPlan).size() >= 3)
            throw new RuntimeException("No se puede asignar más de 3 evaluadores a una evaluación.");

        if(LocalDate.now().isAfter(evaluacionPlan.getFechaCorte())
                && (planNegocio.getEstado().equals(this.estadosCalificacion.getEstados()[0]) ||
                planNegocio.getEstado().equals(this.estadosCalificacion.getEstados()[1]))) {
            throw new RuntimeException("No se pueden asignar docentes evaluadores a una evaluación con fecha corte vencida y que ya se encuentre calificada.");
        }

        if(fechaCorte != null) {
            if(LocalDate.now().isAfter(fechaCorte))
                throw new RuntimeException("No se puede asignar una fecha corte ya vencida a una calificación.");
            if(LocalDate.now().isBefore(evaluacionPlan.getFechaCorte()) || LocalDate.now().isEqual(evaluacionPlan.getFechaCorte()))
                throw new RuntimeException("No se puede crear una calificación con fecha corte, si la fecha corte de la evaluación no ha vencido.");
            evaluacionPlan.setFechaCorte(fechaCorte);
            this.evaluacionPlanServices.actualizar(evaluacionPlan);
        }
        else if(planNegocio.getEstado().equals(this.estadosCalificacion.getEstados()[3]))
            throw new RuntimeException("No se pueden asignar docentes evaluadores a una evaluación vencida sin establecer una nueva fecha Corte");

        CalificacionPlanKey id = new CalificacionPlanKey(docente.getCodigo(), evaluacionPlan.getId());
        Optional<CalificacionPlan> resultado = this.calificacionPlanRepository.findById(id);
        if(resultado.isPresent())
            throw new RuntimeException("El docente seleccionado ya se encuentra asignado.");
        this.calificacionPlanRepository.save(new CalificacionPlan(id, docente, null, evaluacionPlan, this.estadosCalificacion.getEstados()[2], null, LocalDate.now(), evaluacionPlan.getFechaCorte()));
    }

    public List<CalificacionPlan> obtenerCalificacionesDeEvaluacion(EvaluacionPlan evaluacionPlan) {
        try {
            if(evaluacionPlan == null)
                throw new RuntimeException();
            evaluacionPlan = this.evaluacionPlanServices.obtener(evaluacionPlan.getId());
        }
        catch (Exception e) {
            throw new RuntimeException("No se pueden obtener calificaciones de una evaluación que no existe");
        }
        List<CalificacionPlan> calificaciones = this.calificacionPlanRepository.findByEvaluacion(evaluacionPlan.getId());
        for(int i = 0; i < calificaciones.size(); i++) {
            calificaciones.set(i, this.obtener(calificaciones.get(i).getId()));
        }
        String estado = this.estadoSegunCalificaciones(calificaciones, evaluacionPlan.getFechaCorte());
        PlanNegocio planNegocio = evaluacionPlan.getPlanNegocio();
        if(!estado.equals(planNegocio.getEstado())) {
            this.planNegocioServices.actualizarEstado(planNegocio.getTitulo(), estado);
        }
        return calificaciones;
    }

    public CalificacionPlan obtener(CalificacionPlanKey id) {
        if(id == null)
            throw new RuntimeException("Información incompleta para obtener una calificación");
        try {
            Docente docente = this.docenteServices.obtenerDocente(id.getCodigoDocente());
        }
        catch(Exception e) {
            throw new RuntimeException("No se puede obtener una calificación que tiene asignada un docente que no existe.");
        }
        try {
            EvaluacionPlan evaluacionPlan = this.evaluacionPlanServices.obtener(id.getEvaluacionPlanId());
        }
        catch(Exception e) {
            throw new RuntimeException("No se puede obtener una calificación de una evaluacion que no existe.");
        }
        Optional<CalificacionPlan> resultado = this.calificacionPlanRepository.findById(id);
        if(!resultado.isPresent())
            throw new RuntimeException("La calificación que se desea obtener no existe.");
        CalificacionPlan calificacion = resultado.get();
        if(LocalDate.now().isAfter(calificacion.getFechaCorte()) && calificacion.getEstado().equals(this.estadosCalificacion.getEstados()[2])) {
            //La calificación se establece como vencida si se cumplió la fecha corte y tiene un estado pendiente
            calificacion.setEstado(this.estadosCalificacion.getEstados()[3]);
            this.actualizar(calificacion);
        }
        calificacion.setNombreDocente(calificacion.getDocente().getNombre() + " " + calificacion.getDocente().getApellido());
        return calificacion;
    }

    public void actualizar(String titulo, String nota, String observacion, String JWT) {
        Docente docente = this.docenteServices.obtenerDocente(this.encrypt.getJwt().getKey(JWT));
        EvaluacionPlan evaluacionPlan = this.evaluacionPlanServices.obtenerEvaluacionReciente(titulo);
        CalificacionPlanKey id = new CalificacionPlanKey(docente.getCodigo(), evaluacionPlan.getId());
        Optional<CalificacionPlan> resultado = this.calificacionPlanRepository.findById(id);
        if(!resultado.isPresent())
            throw new RuntimeException("El docente no está asignado a la evaluación del plan de negocio consultada");

        CalificacionPlan calificacion = resultado.get();
        if(calificacion.getEstado().equals(this.estadosCalificacion.getEstados()[0]) || calificacion.getEstado().equals(this.estadosCalificacion.getEstados()[1]))
            throw new RuntimeException("No se puede dar una nota a una calificación con un estado de 'aprobada' o 'rechazada'");
        if(!(nota.equals(this.estadosCalificacion.getEstados()[0]) || nota.equals(this.estadosCalificacion.getEstados()[1])))
            throw new RuntimeException("La nota de la calificación solo puede ser 'aprobada' o 'rechazada'");
        if(observacion == null || observacion.isEmpty() || observacion.isBlank())
            throw new RuntimeException("Para calificar un plan de negocio se debe agregar una observación.");

        calificacion.setObservacion(observacion);
        calificacion.setEstado(nota);
        this.actualizar(calificacion);
        //Actualiza estado de la idea de negocio
        this.obtenerCalificacionesDeEvaluacion(evaluacionPlan);
    }

    private void actualizar(CalificacionPlan calificacionPlan) {
        if(calificacionPlan == null || calificacionPlan.getId() == null)
            throw new RuntimeException("Información incompleta para actualizar una calificación.");
        Optional<CalificacionPlan> resultado = this.calificacionPlanRepository.findById(calificacionPlan.getId());
        if(!resultado.isPresent())
            throw new RuntimeException("No se puede actualizar una calificación que no existe");
        this.calificacionPlanRepository.save(calificacionPlan);
    }

    public void eliminar(CalificacionPlanKey calificacionPlanKey) {
        //Al obtener la calificación se actualiza el estado para abordar el caso de que se superó la fecha corte
        CalificacionPlan calificacionPlan = this.obtener(calificacionPlanKey);

        //No se puede eliminar un plan que tenga el estado "aprobada" ni el estado "rechazada"
        if(calificacionPlan.getEstado().equals(this.estadosCalificacion.getEstados()[0]) || calificacionPlan.getEstado().equals(this.estadosCalificacion.getEstados()[1]))
            throw new RuntimeException("No se puede eliminar una calificación que ya este evaluada.");

        //No se puede eliminar una calificacion que se encuentre pendiente
        if(calificacionPlan.getEstado().equals(this.estadosCalificacion.getEstados()[2]))
            throw new RuntimeException("No se puede eliminar una calificación pendiente");

        //En esta sección se verifica que la evaluación aún no este calificada
        EvaluacionPlan evaluacionPlan = this.evaluacionPlanServices.obtener(calificacionPlanKey.getEvaluacionPlanId());
        List<CalificacionPlan> calificaciones = this.obtenerCalificacionesDeEvaluacion(evaluacionPlan);
        String estadoEvaluacion = this.estadoSegunCalificaciones(calificaciones, evaluacionPlan.getFechaCorte());
        if (!(estadoEvaluacion.equals(this.estadosCalificacion.getEstados()[2]) || estadoEvaluacion.equals(this.estadosCalificacion.getEstados()[3])))
            throw new RuntimeException("No se puede eliminar una calificación cuando la evaluación ya esta calificada.");

        this.calificacionPlanRepository.delete(calificacionPlan);
    }

    private String estadoSegunCalificaciones(List<CalificacionPlan> calificaciones, LocalDate fechaCorte) {
        int cnt = 0;
        for(CalificacionPlan calificacion : calificaciones) {
            if(calificacion.getEstado().equals(this.estadosCalificacion.getEstados()[0]))
                cnt++;
            if(calificacion.getEstado().equals(this.estadosCalificacion.getEstados()[1]))
                cnt--;
        }
        if(cnt > 1)
            return this.estadosCalificacion.getEstados()[0];
        if(cnt < -1)
            return this.estadosCalificacion.getEstados()[1];
        // Fecha hoy es después de la fecha corte
        if(LocalDate.now().isAfter(fechaCorte))
            return this.estadosCalificacion.getEstados()[3];
        return this.estadosCalificacion.getEstados()[2];
    }
}
