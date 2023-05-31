package com.backend.softue.services;

import com.backend.softue.models.DocumentoPlan;
import com.backend.softue.models.PlanNegocio;
import com.backend.softue.repositories.DocumentoPlanRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Setter
@Service
public class DocumentoPlanServices {
    @Autowired
    private DocumentoPlanRepository documentoPlanRepository;

    private PlanNegocioServices planNegocioServices;

    public void agregarDocumentoPlan(String titulo, byte[] documento, String nombreArchivo) {
        PlanNegocio planNegocio;
        try {
            planNegocio = this.planNegocioServices.obtenerPlanNegocio(titulo);
        }
        catch (Exception e) {
            throw new RuntimeException("El plan de negocio a la que se le quiere asignar un documento no existe");
        }
        if (documento == null)
            throw new RuntimeException("No se envio ningún documento para ser agregado");
        String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf("."));
        if (!extension.equals(".pdf") && !extension.equals(".docx"))
            throw new RuntimeException("La extensión del archivo no es permitida, por favor subir documentos .docx o .pdf");
        this.documentoPlanRepository.save(new DocumentoPlan(null, documento, planNegocio, nombreArchivo));
    }

    public void eliminarDocumentoPlan(Integer id) {
        if (id == null)
            throw new RuntimeException("No se envió un id para eliminar el documento del plan de negocio");
        Optional<DocumentoPlan> result = this.documentoPlanRepository.findById(id);
        if (!result.isPresent())
            throw new RuntimeException("El documento con el id especificado no existe");
        this.documentoPlanRepository.deleteById(id);
    }

    public DocumentoPlan obtenerDocumentoPlan(String titulo) {
        if (titulo == null)
            throw new RuntimeException("No se envió un titulo con el que buscar el plan de negocio");
        PlanNegocio planNegocio = this.planNegocioServices.obtenerPlanNegocio(titulo);
        Optional<DocumentoPlan> result = this.documentoPlanRepository.findById(planNegocio.getId());
        if (!result.isPresent())
            throw new RuntimeException("El plan de negocio no tiene un documento asignado");
        return result.get();
    }
}
