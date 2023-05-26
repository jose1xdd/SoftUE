package com.backend.softue.services;

import com.backend.softue.models.DocenteApoyoIdea;
import com.backend.softue.models.DocumentoIdea;
import com.backend.softue.models.Estudiante;
import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.models.IdeaPlanteada;
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

    public void crear(IdeaNegocio ideaNegocio, String integrantes[], byte[] documento, String nombreArchivo, String JWT) {
        IdeaNegocio resultado = this.ideaNegocioRepository.findByTitulo(ideaNegocio.getTitulo());
        if (resultado != null)
            throw new RuntimeException("Existe otra idea de negocio con el mismo título");
        if (!this.encrypt.getJwt().getValue(JWT).toLowerCase().equals("estudiante"))
            throw new RuntimeException("No se puede crear una idea de negocio si no se es un estudiante");
        if (!areasConocimiento.getAreasConocimiento().contains(ideaNegocio.getAreaEnfoque()))
            throw  new RuntimeException("No se puede crear esta idea de negocio, el area de conocimiento ingresada no es parte de las comtempladas por el sistema");
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
        if (nombreArchivo != null)
            agregarDocumento(ideaNegocio.getTitulo(), documento, nombreArchivo);
    }

    public void agregarDocumento(String titulo, byte[] documento, String nombreArchivo) {
        if (titulo == null)
            throw new RuntimeException("No se proporciono un titulo para buscar la idea de negocio que se le quiere agregar el documento");
        if (documento == null)
            throw new RuntimeException("No se envió un documento que agregar a la idea de negocio");
        IdeaNegocio ideaNegocio = this.obtenerIdeaNegocio(titulo);
        if (ideaNegocio == null)
            throw new RuntimeException("No existe una idea de negocio con ese nombre");
        if (ideaNegocio.getDocumentoIdea() != null)
            eliminarDocumento(titulo);
        this.documentoIdeaServices.agregarDocumentoIdea(titulo, documento, nombreArchivo);
        DocumentoIdea result = this.documentoIdeaServices.obtenerDocumento(titulo);
        ideaNegocio.setDocumentoIdea(result);
        this.ideaNegocioRepository.save(ideaNegocio);
    }

    public  void eliminarDocumento(String titulo) {
        if (titulo == null)
            throw new RuntimeException("No se proporciono un titulo para buscar la idea de negocio que se le quiere eliminar el documento");
        IdeaNegocio ideaNegocio = this.obtenerIdeaNegocio(titulo);
        ideaNegocio.setDocumentoIdea(null);
        this.ideaNegocioRepository.save(ideaNegocio);
        this.documentoIdeaServices.eliminarDocumentoIdea(ideaNegocio.getId());
    }

    public DocumentoIdea recuperarDocumento(String titulo) {
        if (titulo == null)
            throw new RuntimeException("No se proporciono informacion para buscar el documento correspondiente a la idea con el titulo proporcionado");
        return this.documentoIdeaServices.obtenerDocumento(titulo);
    }

    public IdeaNegocio obtenerIdeaNegocio(String titulo) {
        if (titulo == null)
            throw new RuntimeException("No se enviaron datos para buscar la idea de negocio");
        IdeaNegocio result = this.ideaNegocioRepository.findByTitulo(titulo);
        if (result == null)
            throw new RuntimeException("No exite ninguna idea de negocio con ese titulo");
        if (result.getEstudianteLider() != null)
        result.setEstudianteLiderInfo(new String[][] {{result.getEstudianteLider().getCorreo()}, {result.getEstudianteLider().getNombre() + " " + result.getEstudianteLider().getApellido()}});
        if(result.getEstudiantesIntegrantes() != null){
            Integer ctn = 0;
            String arr [][] = new String[2][result.getEstudiantesIntegrantes().size()];

            for(IdeaPlanteada v : result.getEstudiantesIntegrantes()){
                arr[0][ctn] = v.getEstudiante().getCorreo();
                arr[1][ctn] = v.getEstudiante().getNombre() + " " + v.getEstudiante().getApellido();
                ctn++;
            }
            result.setEstudiantesIntegrantesInfo(arr);
        }
        if (result.getDocentesApoyo() != null) {
            int indice = 0;
            String docentesApoyoInfo[][] = new String[2][result.getDocentesApoyo().size()];
            for (DocenteApoyoIdea docenteApoyoIdea : result.getDocentesApoyo()) {
                docentesApoyoInfo[0][indice] = docenteApoyoIdea.getDocente().getCorreo();
                docentesApoyoInfo[1][indice] = docenteApoyoIdea.getDocente().getNombre() + " " + docenteApoyoIdea.getDocente().getApellido();
                indice++;
            }
            result.setDocentesApoyoInfo(docentesApoyoInfo);
        }
        if(result.getTutor() != null){
            result.setTutorInfo(new String[][]{{result.getTutor().getCorreo()} , {result.getTutor().getNombre() + " " + result.getTutor().getApellido()}});
        }
        return result;
    }

    public void actualizar(String tituloActual, String tituloNuevo , String area, String estado, String jwt) {
        if (tituloActual == null)
            throw new RuntimeException("No se envio el titulo de la idea a modificar");
        if (tituloNuevo == null)
            throw new RuntimeException("No se envio el nuevo titulo de la idea");
        if (area == null)
            throw new RuntimeException("No se envio la nueva area de la idea");
        if(estado == null)
            throw new RuntimeException("No se el estado de la idea");

        IdeaNegocio idea = this.ideaNegocioRepository.findByTitulo(tituloActual);
        if (idea == null)
            throw new RuntimeException("No existe la idea de negocio la cual desea modificar");

        String correo = this.encrypt.getJwt().getKey(jwt);
        if (!correo.equals(idea.getEstudianteLider().getCorreo()))
            throw new RuntimeException("Solo el estudiante lider puede actualizar la idea de negocio");

        if (!areasConocimiento.getAreasConocimiento().contains(area))
            throw new RuntimeException("No se puede actualizar la idea, el area de conocimiento ingresada no es parte de las comtempladas por el sistema");

        idea.setAreaEnfoque(area);
        idea.setTitulo(tituloNuevo);
        idea.setEstado(estado);
        this.ideaNegocioRepository.save(idea);
    }

    private boolean validarIntegrantes(List<Estudiante> integrantes, String lider) {
        Set<String> conjuntoCorreos = new HashSet<>();
        for(Estudiante estudiante : integrantes) {
            conjuntoCorreos.add(estudiante.getCorreo());
        }
        return conjuntoCorreos.size() == integrantes.size() && !conjuntoCorreos.contains(lider);
    }

    public List<IdeaNegocio> listar() {
        List<IdeaNegocio> ideasNegocio = this.ideaNegocioRepository.findAll();
        for(IdeaNegocio ideaNegocio : ideasNegocio) {
            ideaNegocio = this.obtenerIdeaNegocio(ideaNegocio.getTitulo());
        }
        return ideasNegocio;
    }

}
