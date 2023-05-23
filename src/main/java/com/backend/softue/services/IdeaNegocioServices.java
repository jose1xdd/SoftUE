package com.backend.softue.services;

import com.backend.softue.models.DocumentoIdea;
import com.backend.softue.models.Estudiante;
import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.repositories.IdeaNegocioRepository;
import com.backend.softue.security.Hashing;
import com.backend.softue.security.Roles;
import com.backend.softue.utils.AreasConocimiento;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class IdeaNegocioServices {

    @Autowired
    private IdeaNegocioRepository ideaNegocioRepository;

    @Autowired
    private EstudianteServices estudianteServices;

    @Autowired
    private IdeaPlanteadaServices ideaPlanteadaServices;

    @Autowired
    private DocumentoIdeaServices documentoIdeaServices;

    @Autowired
    private Hashing encrypt;

    @Autowired
    private Roles roles;

    @Autowired
    private AreasConocimiento areasConocimiento;

    @PostConstruct
    public void init() {
        this.ideaPlanteadaServices.setIdeaNegocioServices(this);
        this.documentoIdeaServices.setIdeaNegocioServices(this);
    }
    public void crear(IdeaNegocio ideaNegocio, String integrantes[], byte[] documento, String JWT) {
        IdeaNegocio resultado = this.ideaNegocioRepository.findByTitulo(ideaNegocio.getTitulo());
        if (resultado != null)
            throw new RuntimeException("Existe otra idea de negocio con el mismo título");
        if (!this.encrypt.getJwt().getValue(JWT).toLowerCase().equals("estudiante"))
            throw new RuntimeException("No se puede crear una idea de negocio si no se es un estudiante");
        if (!areasConocimiento.getAreasConocimiento().contains(ideaNegocio.getAreaEnfoque()))
            throw  new RuntimeException("No se puede crear este usuario,el area de conocimiento ingresada no es parte de las comtempladas por el sistema");
        List<Estudiante> estudiantesIntegrantes = new LinkedList<Estudiante>();
        try {
            for (String correo : integrantes) {
                estudiantesIntegrantes.add(this.estudianteServices.obtenerEstudiante(correo));
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Los integrantes de la idea de negocio no son validos");
        }

        String correo = this.encrypt.getJwt().getKey(JWT);
        try {
            Estudiante estudiante = this.estudianteServices.obtenerEstudiante(correo);
            ideaNegocio.setEstudianteLider(estudiante);
        }
        catch (Exception e) {
            throw new RuntimeException("El estudiante lider de la idea no existe");
        }
        this.ideaNegocioRepository.save(ideaNegocio);
        this.ideaPlanteadaServices.agregarIntegrantes(ideaNegocio, estudiantesIntegrantes);
        if (documento != null) {
            this.documentoIdeaServices.agregarDocumentoIdea(ideaNegocio, documento);
            DocumentoIdea result = this.documentoIdeaServices.obtenerDocumento(ideaNegocio.getTitulo());
            ideaNegocio.setDocumentoIdea(result);
            this.ideaNegocioRepository.save(ideaNegocio);
        }
    }

    public IdeaNegocio obtenerIdeaNegocio(String titulo) {
        if (titulo == null)
            throw new RuntimeException("No se enviaron datos para buscar la idea de negocio");
        IdeaNegocio result = this.ideaNegocioRepository.findByTitulo(titulo);
        if (result == null)
            throw new RuntimeException("No exite ninguna idea de negocio con ese titulo");
        return result;
    }

    public void actualizar(String tituloActual, String tituloNuevo , String area, String jwt){
        if(tituloActual == null) throw new RuntimeException("No se envio el titulo de la idea a modificar");
        if(tituloNuevo == null) throw new RuntimeException("No se envio el nuevo titulo de la idea");
        if(area == null) throw new RuntimeException("No se envio la nueva area de la idea");

        IdeaNegocio idea = this.ideaNegocioRepository.findByTitulo(tituloActual);
        if (idea == null) throw new RuntimeException("No existe la idea de negocio la cual desea modificar");

        if(!tituloActual.equals(tituloNuevo)){
            IdeaNegocio resultado = this.ideaNegocioRepository.findByTitulo(tituloNuevo);
            if (resultado != null)
                throw new RuntimeException("No se puede actualizar la idea, ya existe una idea con el titulo que desea cambiar.");
        }

        if (!areasConocimiento.getAreasConocimiento().contains(area)) throw  new RuntimeException("No se puede actualizar la idea, el area de conocimiento ingresada no es parte de las comtempladas por el sistema");
        String correo = this.encrypt.getJwt().getKey(jwt);
        if(!correo.equals(idea.getEstudianteLider().getCorreo())) throw new RuntimeException("Solo el estudiante lider puede actualizar la idea de negocio");

        idea.setAreaEnfoque(area);
        idea.setTitulo(tituloNuevo);
        this.ideaNegocioRepository.save(idea);
    }
}
