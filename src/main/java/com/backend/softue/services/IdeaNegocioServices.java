package com.backend.softue.services;

import com.backend.softue.models.DocumentoIdea;
import com.backend.softue.models.Estudiante;
import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.repositories.EstudianteRepository;
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

    /*public void actualizarTitulo(IdeaNegocio ideaNegocio, String JWT) {
        IdeaNegocio resultado = this.ideaNegocioRepository.findById(ideaNegocio.getId()).get();
        if(resultado == null)
            throw new RuntimeException("No existe la idea de negocio a la cuál se le desea actualizar.");
        if(!this.encrypt.getJwt().getKey(JWT).equals(resultado.getEstudianteLider().getCorreo()))
            throw new RuntimeException("Solo el estudiante líder de la idea de negocio puede actualizar el título.");
        resultado.setTitulo(ideaNegocio.getTitulo());

        this.ideaNegocioRepository.save(resultado);
    }

    public void agregarIntegrante(Estudiante estudiante, String titulo, String JWT) {
        IdeaNegocio ideaNegocio = this.ideaNegocioRepository.findByTitulo(titulo);
        if(ideaNegocio == null)
            throw new RuntimeException("No existe la idea de negocio a la cuál se le desea agregar un integrante estudiante");
        if(ideaNegocio.getTutor() != null)
            ideaNegocio.setCorreoTutor(ideaNegocio.getTutor().getCorreo());
        if(!this.encrypt.getJwt().getKey(JWT).equals(ideaNegocio.getCorreoTutor()))
            throw new RuntimeException("Solo el docente tutor de la idea de negocio puede agregar un integrantes.");

        ideaNegocio.setCorreoEstudiantesIntegrantes(new String[] {estudiante.getCorreo()});
        this.ideaPlanteadaServices.crear(ideaNegocio);
    }

    public void eliminarIntegrante(Estudiante estudiante, String titulo, String JWT){
        IdeaNegocio ideaNegocio = this.ideaNegocioRepository.findByTitulo(titulo);
        if(ideaNegocio == null)
            throw new RuntimeException("No existe la idea de negocio a la cuál se le desea eliminar un integrante estudiante");
        if(ideaNegocio.getTutor() != null)
            ideaNegocio.setCorreoTutor(ideaNegocio.getTutor().getCorreo());
        if(!this.encrypt.getJwt().getKey(JWT).equals(ideaNegocio.getCorreoTutor()))
            throw new RuntimeException("Solo el docente tutor de la idea de negocio puede eliminar integrantes.");

        this.ideaPlanteadaServices.eliminar(ideaNegocio, estudiante);
    }*/
}
