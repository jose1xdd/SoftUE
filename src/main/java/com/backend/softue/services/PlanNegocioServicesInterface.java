package com.backend.softue.services;

import com.backend.softue.models.*;
import com.backend.softue.repositories.PlanNegocioRepository;
import com.backend.softue.security.Hashing;
import com.backend.softue.utils.emailModule.EmailService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@Service
public class PlanNegocioServicesInterface {
    @Autowired
    PlanNegocioRepository planNegocioRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    DocenteServices docenteServices;
    @Autowired
    Hashing encrypth;
    @PostConstruct
    public void init() {
        this.docenteServices.getUsuarioServices().setPlanNegocioServicesInterface(this);
    }
    public void asignarTutor(String titulo, String docenteEmail) {
        PlanNegocio planNegocio = this.planNegocioRepository.findByTitulo(titulo).get();
        if(planNegocio.getEstado().equals("aprobada"))
            throw new RuntimeException("No se puede modificar un plan de negocio aprobado");
        Docente docente = this.docenteServices.obtenerDocente(docenteEmail);
        if (docente == null) throw new RuntimeException("No se encontro un docente con ese email");
        this.emailService.enviarEmailTutorPlan(docenteEmail, titulo, docente.getNombre() + " " + docente.getApellido(), docente.getArea());
    }
    public PlanNegocio confirmarTutor(PlanNegocio planNegocio) {
        return this.planNegocioRepository.save(planNegocio);
    }
    public String confirmarTutoria (Boolean respuesta,String titulo, String jwt){
        if(respuesta){
            PlanNegocio planNegocio = this.planNegocioRepository.findByTitulo(titulo).get();
            if(planNegocio == null ) throw  new RuntimeException("se mandó mal el titulo de la idea de negocio");
            Docente docente = this.docenteServices.obtenerDocente(this.encrypth.getJwt().getKey(jwt));
            planNegocio.setTutor(docente);
            Set<DocenteApoyoPlan> docentes = planNegocio.getDocentesApoyo();
            if(docentePertenece(docentes,docente)) throw new RuntimeException("El docente tutor no puede ser docente de apoyo");
            if(this.confirmarTutor(planNegocio) != null);return "Docente Asignado";
        }
        return "El docente rechazo";
    }
    private Boolean docentePertenece(Set<DocenteApoyoPlan> docentes ,Docente docente){
        Iterator<DocenteApoyoPlan> it = docentes.iterator();
        while (it.hasNext()){
            if(it.next().getDocente().equals(docente)) return true;
        }
        return  false;
    }
}
