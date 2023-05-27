package com.backend.softue.services;

import com.backend.softue.models.*;
import com.backend.softue.repositories.DocenteApoyoPlanRepository;
import com.backend.softue.security.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocenteApoyoPlanServices {
    @Autowired
    private DocenteApoyoPlanRepository docenteApoyoPlanRepository;

    @Autowired
    private  PlanNegocioServices planNegocioServices;
    @Autowired
    private DocenteServices docenteServices;
    @Autowired
    private Hashing encrypt;

    public void agregarDocenteApoyo(String jwt, String tituloPlan, String correoDocente) {
        if (tituloPlan == null)
            throw new RuntimeException("No se envió un titulo con el que buscar el plan de negocio");
        if (correoDocente == null)
            throw new RuntimeException("No se envió un correo con el que buscar al docente");
        PlanNegocio planNegocio = this.planNegocioServices.obtenerPlanNegocio(tituloPlan);
        if (planNegocio.getTutor() == null || !this.encrypt.getJwt().getKey(jwt).equals(planNegocio.getTutor().getCorreo()))
            throw new RuntimeException("Solo el tutor de un plan de negocio puede gestionar los docentes de apoyo del mismo");
        if(planNegocio.getTutor().getCorreo().equals(correoDocente))
            throw new RuntimeException("No se puede asignar al tutor como un docente de apoyo");
        Docente docente = this.docenteServices.obtenerDocente(correoDocente);
        this.docenteApoyoPlanRepository.save(new DocenteApoyoPlan(new DocentePlanKey(docente.getCodigo(), planNegocio.getId()), docente, planNegocio));
    }

    public void eliminarDocenteApoyo(String jwt, String tituloPlan, String correoDocente) {
        if (tituloPlan == null)
            throw new RuntimeException("No se envió un titulo con el que buscar el plan de negocio");
        if (correoDocente == null)
            throw new RuntimeException("No se envió un correo con el que buscar al docente");
        PlanNegocio planNegocio = this.planNegocioServices.obtenerPlanNegocio(tituloPlan);
        if (!this.encrypt.getJwt().getKey(jwt).equals(planNegocio.getTutor().getCorreo()))
            throw new RuntimeException("Solo el tutor de una idea de negocio puede gestionar los docentes de apoyo del mismo");
        Docente docente = this.docenteServices.obtenerDocente(correoDocente);
        DocenteApoyoPlan docenteApoyoPlan = this.docenteApoyoPlanRepository.getReferenceById(new DocentePlanKey(docente.getCodigo(), planNegocio.getId()));
        this.docenteApoyoPlanRepository.delete(docenteApoyoPlan);
    }
}
