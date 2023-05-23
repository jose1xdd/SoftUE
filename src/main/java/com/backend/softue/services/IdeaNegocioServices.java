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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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

        if(!this.validarIntegrantes(estudiantesIntegrantes, correo))
            throw new RuntimeException("Los integrantes seleccionados son invalidos");

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

    private boolean validarIntegrantes(List<Estudiante> integrantes, String lider) {
        boolean valido;

        Set<String> conjuntoCorreos = new HashSet<>();
        for(Estudiante estudiante : integrantes) {
            conjuntoCorreos.add(estudiante.getCorreo());
        }
        valido = conjuntoCorreos.size() == integrantes.size() && !conjuntoCorreos.contains(lider);

        return valido;
    }

}
