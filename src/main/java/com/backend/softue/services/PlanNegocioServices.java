package com.backend.softue.services;

import com.backend.softue.models.DocumentoIdea;
import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.models.PlanNegocio;
import com.backend.softue.repositories.PlanNegocioRepository;
import com.backend.softue.security.Hashing;
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
        this.planNegocioRepository.save(new PlanNegocio(ideaNegocio.getId(), ideaNegocio.getTitulo(), null, 'F', ideaNegocio.getAreaEnfoque(), ideaNegocio.getTutor(), LocalDate.now(), ideaNegocio.getEstudianteLider(), null, null, null, null, null));
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

    /*public void agregarDocumento(String titulo, byte[] documento, String nombreArchivo) {
        if (titulo == null)
            throw new RuntimeException("No se proporciono un titulo para buscar el plan de negocio que se le quiere agregar el documento");
        if (documento == null)
            throw new RuntimeException("No se envió un documento que agregar al plan de negocio");
        PlanNegocio planNegocio = this.obtenerPlanNegocio(titulo);
        if (planNegocio == null)
            throw new RuntimeException("No existe un plan de negocio con ese nombre");
        //if (planNegocio.getDocumentoIdea() != null)
        //    eliminarDocumento(titulo);
        this.documentoIdeaServices.agregarDocumentoIdea(titulo, documento, nombreArchivo);
        DocumentoIdea result = this.documentoIdeaServices.obtenerDocumento(titulo);
        ideaNegocio.setDocumentoIdea(result);
        this.ideaNegocioRepository.save(ideaNegocio);
    }*/
}