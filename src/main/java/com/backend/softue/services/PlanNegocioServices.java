package com.backend.softue.services;

import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.models.PlanNegocio;
import com.backend.softue.repositories.PlanNegocioRepository;
import com.backend.softue.security.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PlanNegocioServices {

    @Autowired
    private PlanNegocioRepository planNegocioRepository;

    @Autowired
    private IdeaNegocioServices ideaNegocioServices;

    @Autowired
    private Hashing encrypt;
    public void crear(String jwt, String titulo) {
        if (titulo == null)
            throw new RuntimeException("No se envio el titulo de la idea de negocio del que se va crear el plan");
        if (!this.encrypt.getJwt().getValue(jwt).toLowerCase().equals("coordinador"))
            throw new RuntimeException("No se puede crear un plan de negocio si no se es el coordinador de la unidad de emprendimiento");
        IdeaNegocio ideaNegocio = this.ideaNegocioServices.obtenerIdeaNegocio(titulo);
        if (ideaNegocio == null)
            throw new RuntimeException("No existe una idea de negocio con ese titulo");
        //if (!ideaNegocio.getEstado().equals("aprobada"))
            //throw new RuntimeException("No se puede un plan a partir de una idea de negocio no aprobada");
        this.planNegocioRepository.save(new PlanNegocio(ideaNegocio.getId(), ideaNegocio.getTitulo(), "Esto es un resumen", 'F', ideaNegocio.getAreaEnfoque(), ideaNegocio.getTutor(), LocalDate.now(), ideaNegocio.getEstudianteLider(), null, null, null, null, null));
    }
}
