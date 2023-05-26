package com.backend.softue.services;

import com.backend.softue.models.DocumentoIdea;
import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.repositories.DocumentoIdeaRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Setter
@Service
public class DocumentoIdeaServices {

    @Autowired
    private DocumentoIdeaRepository documentoIdeaRepository;

    private IdeaNegocioServices ideaNegocioServices;

    public void agregarDocumentoIdea(String titulo, byte[] documento, String nombreArchivo) {
        IdeaNegocio ideaNegocio;
        try {
            ideaNegocio = this.ideaNegocioServices.obtenerIdeaNegocio(titulo);
        }
        catch (Exception e) {
            throw new RuntimeException("La idea de negocio a la que se le quiere asignar un documento no existe");
        }
        if (documento == null)
            throw new RuntimeException("No se envio ningún documento para ser agregado");
        String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf("."));
        if (!extension.equals(".pdf") && !extension.equals(".docx"))
            throw new RuntimeException("La extensión del archivo no es permitida, por favor subir documentos .docx o .pdf");
        this.documentoIdeaRepository.save(new DocumentoIdea(null, documento, ideaNegocio, nombreArchivo));
    }

    public void eliminarDocumentoIdea(Integer id) {
        if (id == null)
            throw new RuntimeException("No se envió un id para eliminar el documento de idea de negocio");
        Optional<DocumentoIdea> result = this.documentoIdeaRepository.findById(id);
        if (!result.isPresent())
            throw new RuntimeException("El documento con el id especificado no existe");
        this.documentoIdeaRepository.deleteById(id);
    }

    public DocumentoIdea obtenerDocumento(String titulo) {
        if (titulo == null)
            throw new RuntimeException("No se envió un titulo con el que buscar la idea de negocio");
        IdeaNegocio ideaNegocio = this.ideaNegocioServices.obtenerIdeaNegocio(titulo);
        Optional<DocumentoIdea> result = this.documentoIdeaRepository.findById(ideaNegocio.getId());
        if (!result.isPresent())
            throw new RuntimeException("La idea de negocio no tiene un documento asignado");
        return result.get();
    }
}
