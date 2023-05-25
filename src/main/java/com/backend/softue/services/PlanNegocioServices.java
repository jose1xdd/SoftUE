package com.backend.softue.services;

import com.backend.softue.models.*;
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
        this.planNegocioRepository.save(new PlanNegocio(ideaNegocio.getId(), ideaNegocio.getTitulo(), "Esto es un resumen", 'F', ideaNegocio.getAreaEnfoque(), ideaNegocio.getTutor(), null, LocalDate.now(), ideaNegocio.getEstudianteLider(), null, null, null, null, null,null,null,null));
    }

    public PlanNegocio obtenerPlanNegocio(String titulo){
        if(titulo == null)
            throw new RuntimeException("No se enviaron datos para buscar el plan de negocio");

        PlanNegocio planNegocio = this.planNegocioRepository.findByTitulo(titulo);
        if(planNegocio == null)
            throw new RuntimeException("No se encontro un plan de negocio correspondiente a ese titulo");

        if (planNegocio.getEstudianteLider() != null)
            planNegocio.setEstudianteLiderInfo(new String[][]{{planNegocio.getEstudianteLider().getCorreo()} , {planNegocio.getEstudianteLider().getNombre()+ " " + planNegocio.getEstudianteLider().getApellido()}});

        if(planNegocio.getTutor() != null)
            planNegocio.setTutorInfo(new String[][]{{planNegocio.getTutor().getCorreo()},{planNegocio.getTutor().getNombre() + " " + planNegocio.getTutor().getApellido()}});

        if(planNegocio.getEstudiantesIntegrantes() != null){
            Integer ctn = 0;
            String arr [][] = new String[2][planNegocio.getEstudiantesIntegrantes().size()];

            for(PlanPresentado v : planNegocio.getEstudiantesIntegrantes()){
                arr[0][ctn] = v.getEstudiante().getCorreo();
                arr[1][ctn] = v.getEstudiante().getNombre()+ " " + v.getEstudiante().getApellido();
                ctn++;
            }
            planNegocio.setEstudiantesIntegrantesInfo(arr);
        }
        if (planNegocio.getDocentesApoyo() != null) {
            int indice = 0;
            String docentesApoyoInfo[][] = new String[2][planNegocio.getDocentesApoyo().size()];
            for (DocenteApoyoPlan docenteApoyoPlan : planNegocio.getDocentesApoyo()) {
                docentesApoyoInfo[0][indice] = docenteApoyoPlan.getDocente().getCorreo();
                docentesApoyoInfo[1][indice] = docenteApoyoPlan.getDocente().getNombre() + docenteApoyoPlan.getDocente().getApellido();
                indice++;
            }
            planNegocio.setDocentesApoyoInfo(docentesApoyoInfo);
        }
    return planNegocio;
    }
}
