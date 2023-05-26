package com.backend.softue.services;

import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.models.PlanNegocio;
import com.backend.softue.models.*;
import com.backend.softue.repositories.PlanNegocioRepository;
import com.backend.softue.security.Hashing;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PlanNegocioServices {

    @Autowired
    private PlanNegocioRepository planNegocioRepository;

    @Autowired
    private IdeaNegocioServices ideaNegocioServices;

    @Autowired
    private DocumentoPlanServices documentoPlanServices;

    @Autowired
    private Hashing encrypt;

    @PostConstruct
    public void init() {
        this.documentoPlanServices.setPlanNegocioServices(this);
    }

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
        this.planNegocioRepository.save(new PlanNegocio(ideaNegocio.getId(), ideaNegocio.getTitulo(), null, 'F', ideaNegocio.getAreaEnfoque(), ideaNegocio.getTutor(), null, LocalDate.now(), ideaNegocio.getEstudianteLider(), null, null, null, null, null,null,null,null));
    }

    public PlanNegocio obtenerPlanNegocio(String titulo){
        if(titulo == null)
            throw new RuntimeException("No se enviaron datos para buscar el plan de negocio");

        PlanNegocio planNegocio = this.planNegocioRepository.findByTitulo(titulo).get();
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

    public void actualizarResumen(String titulo, String resumen, String jwt) {
        if (titulo == null)
            throw new RuntimeException("No se envio el titulo del plan de negocio que se desea actulizar el resumen");
        if (resumen == null || resumen.equals(""))
            throw new RuntimeException("No se envio información para actualizar el resumen del plan de negocio");
        Optional<PlanNegocio> resultado = this.planNegocioRepository.findByTitulo(titulo);
        if (!resultado.isPresent())
            throw new RuntimeException("No existe un plan de negocio con ese titulo");
        if (!this.encrypt.getJwt().getKey(jwt).equals(resultado.get().getEstudianteLider().getCorreo()))
            throw new RuntimeException("Solo el estudiante lider puede actualizar el resumen del plan de negocio");
        resultado.get().setResumen(resumen);
        this.planNegocioRepository.save(resultado.get());
    }

    public void agregarDocumento(String titulo, byte[] documento, String nombreArchivo) {
        if (titulo == null)
            throw new RuntimeException("No se proporciono un titulo para buscar el plan de negocio que se le quiere agregar el documento");
        if (documento == null)
            throw new RuntimeException("No se envió un documento que agregar al plan de negocio");
        PlanNegocio planNegocio = this.obtenerPlanNegocio(titulo);
        if (planNegocio == null)
            throw new RuntimeException("No existe un plan de negocio con ese nombre");
        if (planNegocio.getDocumentoPlan() != null)
            eliminarDocumento(titulo);
        this.documentoPlanServices.agregarDocumentoPlan(titulo, documento, nombreArchivo);
        DocumentoPlan result = this.documentoPlanServices.obtenerDocumentoPlan(titulo);
        planNegocio.setDocumentoPlan(result);
        this.planNegocioRepository.save(planNegocio);
    }

    public  void eliminarDocumento(String titulo) {
        if (titulo == null)
            throw new RuntimeException("No se proporciono un titulo para buscar el plan de negocio que se le quiere eliminar el documento");
        PlanNegocio planNegocio = this.obtenerPlanNegocio(titulo);
        planNegocio.setDocumentoPlan(null);
        this.planNegocioRepository.save(planNegocio);
        this.documentoPlanServices.eliminarDocumentoPlan(planNegocio.getId());
    }

    public DocumentoPlan recuperarDocumento(String titulo) {
        if (titulo == null)
            throw new RuntimeException("No se proporciono informacion para buscar el documento correspondiente al plan con el titulo proporcionado");
        return this.documentoPlanServices.obtenerDocumentoPlan(titulo);
    }
}