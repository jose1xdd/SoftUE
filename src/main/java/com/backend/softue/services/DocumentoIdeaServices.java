package com.backend.softue.services;

import com.backend.softue.models.DocumentoIdea;
import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.repositories.DocumentoIdeaRepository;
import jakarta.annotation.PostConstruct;
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

    public void agregarDocumentoIdea(IdeaNegocio ideaNegocio, byte[] documento) {
        try {
            ideaNegocio = ideaNegocioServices.obtenerIdeaNegocio(ideaNegocio.getTitulo());
        }
        catch (Exception e) {
            throw new RuntimeException("La idea de negocio a la que se le quiere asignar un documento no existe");
        }
        if (documento == null)
            throw new RuntimeException("No se envio ningún documento para ser agregado");
        this.documentoIdeaRepository.save(new DocumentoIdea(null, documento, ideaNegocio));
    }

    public DocumentoIdea obtenerDocumento(String titulo) {
        if (titulo == null)
            throw new RuntimeException("No se envió un titulo con el que buscar la idea de negocio");
        IdeaNegocio ideaNegocio = ideaNegocioServices.obtenerIdeaNegocio(titulo);
        Optional<DocumentoIdea> result = this.documentoIdeaRepository.findById(ideaNegocio.getId());
        if (!result.isPresent())
            throw new RuntimeException("La idea de negocio no tiene un documento asignado");
        return result.get();
    }
}
