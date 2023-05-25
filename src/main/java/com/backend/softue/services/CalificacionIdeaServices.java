package com.backend.softue.services;

import com.backend.softue.models.*;
import com.backend.softue.repositories.CalificacionIdeaRepository;
import com.backend.softue.security.Hashing;
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

    @Autowired
    private Hashing encrypt;

    @PostConstruct
    public void init() {
        this.evaluacionIdeaServices.setCalificacionIdeaServices(this);
    }

    public void crear(String titulo, String correo, LocalDate fechaCorte) {
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

        if(this.obtenerCalificacionesDeEvaluacion(evaluacionIdea).size() >= 3)
            throw new RuntimeException("No se puede asignar más de 3 evaluadores a una evaluación.");

        if(fechaCorte != null) {
            if(LocalDate.now().isAfter(fechaCorte))
                throw new RuntimeException("No se puede asignar una fecha corte ya vencida a una calificación.");
            if(LocalDate.now().isBefore(evaluacionIdea.getFechaCorte()))
                throw new RuntimeException("No se puede crear una calificación con fecha corte, si la fecha corte de la evaluación no ha vencido.");
            evaluacionIdea.setFechaCorte(fechaCorte);
            this.evaluacionIdeaServices.actualizar(evaluacionIdea);
        }
        this.calificacionIdeaRepository.save(new CalificacionIdea(new CalificacionIdeaKey(docente.getCodigo(), evaluacionIdea.getId()), docente, evaluacionIdea, this.estadosCalificacion.getEstados()[2], null, LocalDate.now(), evaluacionIdea.getFechaCorte()));
    }

    public List<CalificacionIdea> obtenerCalificacionesDeEvaluacion(EvaluacionIdea evaluacionIdea) {
        try {
            if(evaluacionIdea == null)
                throw new RuntimeException();
            evaluacionIdea = this.evaluacionIdeaServices.obtener(evaluacionIdea.getId());
        }
        catch (Exception e) {
            throw new RuntimeException("No se pueden obtener calificaciones de una evaluación que no existe");
        }
        List<CalificacionIdea> calificaciones = this.calificacionIdeaRepository.findByEvaluacion(evaluacionIdea.getId());
        for(int i = 0; i < calificaciones.size(); i++) {
            calificaciones.set(i, this.obtener(calificaciones.get(i).getId()));
        }
        return calificaciones;
    }

    public CalificacionIdea obtener(CalificacionIdeaKey id) {
        if(id == null)
            throw new RuntimeException("Información incompleta para obtener una calificación");
        try {
            Docente docente = this.docenteServices.obtenerDocente(id.getCodigoDocente());
        }
        catch(Exception e) {
            throw new RuntimeException("No se puede obtener una calificación que tiene asignada un docente que no existe.");
        }
        try {
            EvaluacionIdea evaluacionIdea = this.evaluacionIdeaServices.obtener(id.getEvaluacionIdeaId());
        }
        catch(Exception e) {
            throw new RuntimeException("No se puede obtener una calificación de una evaluacion que no existe.");
        }
        Optional<CalificacionIdea> resultado = this.calificacionIdeaRepository.findById(id);
        if(!resultado.isPresent())
            throw new RuntimeException("La calificación que se desea obtener no existe.");
        CalificacionIdea calificacion = resultado.get();
        if(LocalDate.now().isAfter(calificacion.getFechaCorte()) && calificacion.getEstado().equals(this.estadosCalificacion.getEstados()[2])) {
            //La calificación se establece como vencida si se cumplió la fecha corte y tiene un estado pendiente
            calificacion.setEstado(this.estadosCalificacion.getEstados()[3]);
            this.actualizar(calificacion);
        }
        return resultado.get();
    }

    public void actualizar(String titulo, String nota, String observacion, String JWT) {
        Docente docente = this.docenteServices.obtenerDocente(this.encrypt.getJwt().getKey(JWT));
        EvaluacionIdea evaluacionIdea = this.evaluacionIdeaServices.obtenerEvaluacionReciente(titulo);
        CalificacionIdeaKey id = new CalificacionIdeaKey(docente.getCodigo(), evaluacionIdea.getId());
        Optional<CalificacionIdea> resultado = this.calificacionIdeaRepository.findById(id);
        if(!resultado.isPresent())
            throw new RuntimeException("El docente no está asignado a la evaluación de la idea de negocio consultada");
        CalificacionIdea calificacion = resultado.get();
        if(calificacion.getEstado().equals(this.estadosCalificacion.getEstados()[0]) || nota.equals(this.estadosCalificacion.getEstados()[1]))
            throw new RuntimeException("No se puede dar una nota a una calificación con un estado de 'aprobada' o 'rechazada'");
        if(!(nota.equals(this.estadosCalificacion.getEstados()[0]) || nota.equals(this.estadosCalificacion.getEstados()[1])))
            throw new RuntimeException("La nota de la calificación solo puede ser 'aprobada' o 'rechazada'");
        calificacion.setObservacion(observacion);
        calificacion.setEstado(nota);
        actualizar(calificacion);
    }

    public void actualizar(CalificacionIdea calificacionIdea) {
        if(calificacionIdea == null || calificacionIdea.getId() == null)
            throw new RuntimeException("Información incompleta para actualizar una calificación.");
        Optional<CalificacionIdea> resultado = this.calificacionIdeaRepository.findById(calificacionIdea.getId());
        if(!resultado.isPresent())
            throw new RuntimeException("No se puede actualizar una calificación que no existe");
        this.calificacionIdeaRepository.save(calificacionIdea);
    }

    public void eliminar(CalificacionIdeaKey calificacionIdeaKey) {
        CalificacionIdea calificacionIdea = this.obtener(calificacionIdeaKey);
        //No se puede eliminar una idea que tenga el estado "aprobada" ni el estado "rechazada"
        if(calificacionIdea.getEstado().equals(this.estadosCalificacion.getEstados()[0]) || calificacionIdea.getEstado().equals(this.estadosCalificacion.getEstados()[1]))
            throw new RuntimeException("No se puede eliminar una calificación que ya este evaluada.");
        if(calificacionIdea.getEstado().equals(this.estadosCalificacion.getEstados()[2]))
            throw new RuntimeException("No se puede eliminar una calificación pendiente");
        EvaluacionIdea evaluacionIdea = this.evaluacionIdeaServices.obtener(calificacionIdeaKey.getEvaluacionIdeaId());
        List<CalificacionIdea> calificaciones = this.obtenerCalificacionesDeEvaluacion(evaluacionIdea);
        int cnt = 0;
        for(CalificacionIdea calificacion : calificaciones) {
            if(calificacion.getEstado().equals(this.estadosCalificacion.getEstados()[0]))
                cnt++;
            if(calificacion.getEstado().equals(this.estadosCalificacion.getEstados()[1]))
                cnt--;
        }
        if (cnt != 0)
            throw new RuntimeException("No se puede eliminar una calificación cuando la evaluación ya esta calificada.");
        this.calificacionIdeaRepository.delete(calificacionIdea);
    }
}
