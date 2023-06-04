package com.backend.softue.services;

import com.backend.softue.models.*;
import com.backend.softue.repositories.ObservacionPlanRepository;
import com.backend.softue.security.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;


@Service
public class ObservacionPlanServices {
    @Autowired
    private ObservacionPlanRepository observacionPlanRepository;
    @Autowired
    private PlanNegocioServices planNegocioServices;
    @Autowired
    private DocenteServices docenteServices;

    @Autowired
    private Hashing encrypt;

    public void crearObservacion(String jwt, String planTitulo, String observacion){
        String correoDocente = this.encrypt.getJwt().getKey(jwt);

        if(planTitulo == null)
            throw new RuntimeException("No se suministro un titulo del plan de negocio");
        if(observacion == null)
            throw new RuntimeException("No se suministro una observacion");

        PlanNegocio planNegocio = this.planNegocioServices.obtenerPlanNegocio(planTitulo);
        if(planNegocio.getEstado().equals("aprobada"))
            throw new RuntimeException("No se puede modificar un plan de negocio aprobado.");
        Docente docente = this.docenteServices.obtenerDocente(correoDocente);

        if(!permisosCrear(planNegocio,correoDocente))
            throw new RuntimeException("El docente no tiene permisos para realizar la observacion");

        this.observacionPlanRepository.save(new ObservacionPlan(null,planNegocio,null,docente,null,observacion, LocalDateTime.now()));
    }

    public Set<ObservacionPlan> obtenerObservaciones(String jwt, String titulo){
        if(titulo == null)
            throw new RuntimeException("No se suministro un titulo de idea de negocio");

        PlanNegocio planNegocio = this.planNegocioServices.obtenerPlanNegocio(titulo);
        if(!this.permisosObtener(jwt,planNegocio))
            throw new RuntimeException("No se cuentan con los permisos necesarios");

        Set<ObservacionPlan> result = this.observacionPlanRepository.findByPlanNegocioId(planNegocio);
        if(result != null){
            for(ObservacionPlan v : result){
                v.setTitulo(v.getPlanNegocioId().getTitulo());
                v.setDocenteInfo(new String[][]{{v.getDocenteId().getCorreo()},{v.getDocenteId().getNombre() + " " + v.getDocenteId().getApellido()}});
            }
        }
        return result;
    }

    private boolean permisosObtener(String jwt, PlanNegocio plan){
        String correo = this.encrypt.getJwt().getKey(jwt);
        String rol =  this.encrypt.getJwt().getValue(jwt);

        if(plan.getEstudianteLider().getCorreo().equals(correo)) return true;
        if(plan.getTutor().getCorreo().equals(correo)) return true;
        if((rol.equals("administrativo") || rol.equals("coordinador"))) return true;

        if(plan.getDocentesApoyo() != null){
            for(DocenteApoyoPlan v : plan.getDocentesApoyo()){
                if(v.getDocente().getCorreo().equals(correo))  return true;
            }
        }

        if(plan.getEstudiantesIntegrantes() != null){
            for(PlanPresentado v : plan.getEstudiantesIntegrantes()){
                if(v.getEstudiante().equals(correo))  return true;
            }
        }
        return false;
    }
    private boolean permisosCrear(PlanNegocio plan, String correoDocente) {
        boolean permisos = false;
        if(plan.getTutor() != null) permisos = plan.getTutor().getCorreo().equals(correoDocente);
        if (plan.getDocentesApoyo() != null){
            for (DocenteApoyoPlan v : plan.getDocentesApoyo()) {
                if (v.getDocente().getCorreo().equals(correoDocente)) permisos = true;
            }
        }
        return permisos;
    }
}
