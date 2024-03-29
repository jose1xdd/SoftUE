package com.backend.softue.services;

import com.backend.softue.models.*;
import com.backend.softue.repositories.IdeaNegocioRepository;
import com.backend.softue.security.Hashing;
import com.backend.softue.utils.beansAuxiliares.EstadosIdeaPlanNegocio;
import com.backend.softue.utils.emailModule.EmailService;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;


@Setter
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
    private EstadosIdeaPlanNegocio estadosIdeaPlanNegocio;

    @Autowired
    private EvaluacionIdeaServices evaluacionIdeaServices;

    private DocenteServices docenteServices;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AreaConocimientoServices areaConocimientoServices;

    @PostConstruct
    public void init() {
        this.ideaPlanteadaServices.setIdeaNegocioServices(this);
        this.documentoIdeaServices.setIdeaNegocioServices(this);
        this.evaluacionIdeaServices.setIdeaNegocioServices(this);
        this.estudianteServices.getUsuarioServices().setIdeaNegocioServices(this);
    }

    public void crear(IdeaNegocio ideaNegocio, String area, String integrantes[], byte[] documento, String nombreArchivo, String JWT) {
        IdeaNegocio resultado = this.ideaNegocioRepository.findByTitulo(ideaNegocio.getTitulo());
        if (resultado != null)
            throw new RuntimeException("Existe otra idea de negocio con el mismo título");
        if (!this.encrypt.getJwt().getValue(JWT).toLowerCase().equals("estudiante"))
            throw new RuntimeException("No se puede crear una idea de negocio si no se es un estudiante");
        AreaConocimiento areaConocimiento = null;
        try {
            areaConocimiento = this.areaConocimientoServices.obtener(area);
        }
        catch (Exception e) {
            throw  new RuntimeException("No se puede crear esta idea de negocio, el area de conocimiento ingresada no es parte de las comtempladas por el sistema");
        }
        ideaNegocio.setArea(areaConocimiento);
        List<Estudiante> estudiantesIntegrantes = new LinkedList<Estudiante>();
        try {
            for (String correo : integrantes) {
                estudiantesIntegrantes.add(this.estudianteServices.obtenerEstudiante(correo));
            }
        } catch (Exception e) {
            throw new RuntimeException("Los integrantes de la idea de negocio no son validos");
        }

        String correo = this.encrypt.getJwt().getKey(JWT);
        try {
            Estudiante estudiante = this.estudianteServices.obtenerEstudiante(correo);
            ideaNegocio.setEstudianteLider(estudiante);
        } catch (Exception e) {
            throw new RuntimeException("El estudiante lider de la idea no existe");
        }

        if (!this.validarIntegrantes(estudiantesIntegrantes, correo))
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
        if(ideaNegocio.getEstado().equals("aprobada"))
            throw new RuntimeException("No se puede modificar el documento de una idea de negocio aprobada");
        if (ideaNegocio.getDocumentoIdea() != null)
            eliminarDocumento(titulo);
        this.documentoIdeaServices.agregarDocumentoIdea(titulo, documento, nombreArchivo);
        DocumentoIdea result = this.documentoIdeaServices.obtenerDocumento(titulo);
        ideaNegocio.setDocumentoIdea(result);
        this.ideaNegocioRepository.save(ideaNegocio);
    }

    public void eliminarDocumento(String titulo) {
        if (titulo == null)
            throw new RuntimeException("No se proporciono un titulo para buscar la idea de negocio que se le quiere eliminar el documento");
        IdeaNegocio ideaNegocio = this.obtenerIdeaNegocio(titulo);
        if(ideaNegocio.getEstado().equals("aprobada"))
            throw new RuntimeException("No se puede modificar el documento de una idea de negocio aprobada");
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
        if(result.getEstudiantesIntegrantes() != null) {
            Integer ctn = 0;
            String arr[][] = new String[2][result.getEstudiantesIntegrantes().size()];

            for (IdeaPlanteada v : result.getEstudiantesIntegrantes()) {
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
        try{
            this.evaluacionIdeaServices.obtenerEvaluacionReciente(result);
        }
        catch (Exception e) {}
        result.setAreaEnfoque(result.getArea().getNombre());
        try {
            EvaluacionIdea evaluacionIdea = this.evaluacionIdeaServices.obtenerEvaluacionReciente(result);
            result.setFechaCorte(evaluacionIdea.getFechaCorte());
        }
        catch (Exception e) {}
        return result;
    }

    public void actualizar(String tituloActual, String tituloNuevo, String area, String jwt) {
        if (tituloActual == null)
            throw new RuntimeException("No se envio el titulo de la idea a modificar");
        if (tituloNuevo != null && !tituloActual.equals(tituloNuevo) && this.ideaNegocioRepository.findByTitulo(tituloNuevo) != null)
            throw new RuntimeException("Existe ya una idea con ese mismo nombre");

        IdeaNegocio idea = this.ideaNegocioRepository.findByTitulo(tituloActual);
        if (idea == null)
            throw new RuntimeException("No existe la idea de negocio la cual desea modificar");
        if(idea.getEstado().equals("aprobada"))
            throw new RuntimeException("No se puede modificar una idea de negocio aprobada");
        String correo = this.encrypt.getJwt().getKey(jwt);
        if (!correo.equals(idea.getEstudianteLider().getCorreo()))
            throw new RuntimeException("Solo el estudiante lider puede actualizar la idea de negocio");

        if (tituloNuevo == null || tituloNuevo.isBlank())
            tituloNuevo = idea.getTitulo();

        if (area != null && !area.isBlank() && !this.areaConocimientoServices.existe(area))
            throw new RuntimeException("No se puede actualizar la idea, el area de conocimiento ingresada no es parte de las comtempladas por el sistema");

        AreaConocimiento areaConocimiento = null;
        try {
            areaConocimiento = this.areaConocimientoServices.obtener(area);
        }
        catch (Exception e) {
            areaConocimiento = this.areaConocimientoServices.obtener(idea.getArea().getNombre());
        }

        idea.setArea(areaConocimiento);
        idea.setTitulo(tituloNuevo);
        this.ideaNegocioRepository.save(idea);
    }

    public void actualizarEstado(String titulo, String estado) {
        if(!this.estadosIdeaPlanNegocio.getEstados().contains(estado))
            throw new RuntimeException("No se puede actualizar un estado que no exista");
        IdeaNegocio ideaNegocio = this.ideaNegocioRepository.findByTitulo(titulo);
        ideaNegocio.setEstado(estado);
        this.ideaNegocioRepository.save(ideaNegocio);
    }

    private boolean validarIntegrantes(List<Estudiante> integrantes, String lider) {
        Set<String> conjuntoCorreos = new HashSet<>();
        for (Estudiante estudiante : integrantes) {
            conjuntoCorreos.add(estudiante.getCorreo());
        }
        return conjuntoCorreos.size() == integrantes.size() && !conjuntoCorreos.contains(lider);
    }


    public List<IdeaNegocio> buscarIdeasPorFiltros(String tutorCodigo,String codigoEstudiante, String area, String estado, LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaFin == null ^ fechaInicio == null)
            throw new RuntimeException("Una o las dos fechas del filtro son nulas");
        List<IdeaNegocio> ideasNegocio = this.ideaNegocioRepository.findByFilters(tutorCodigo,codigoEstudiante,area,estado,fechaInicio,fechaFin);
        for(IdeaNegocio ideaNegocio : ideasNegocio){
            ideaNegocio = this.obtenerIdeaNegocio(ideaNegocio.getTitulo());
        }
        return ideasNegocio;
    }

    public List<IdeaNegocio> listar() {
        List<IdeaNegocio> ideasNegocio = this.ideaNegocioRepository.findAll();
        for (IdeaNegocio ideaNegocio : ideasNegocio) {
            ideaNegocio = this.obtenerIdeaNegocio(ideaNegocio.getTitulo());
        }
        return ideasNegocio;
    }

    public void asignarTutor(String titulo, String docenteEmail) {
        IdeaNegocio ideaNegocio = this.obtenerIdeaNegocio(titulo);
        if(ideaNegocio.getEstado().equals("aprobada"))
            throw new RuntimeException("No se puede modificar una idea de negocio aprobada");
        Docente docente = this.docenteServices.obtenerDocente(docenteEmail);
        if (docente == null) throw new RuntimeException("No se encontro un docente con ese email");
        Set<DocenteApoyoIdea> docentes = ideaNegocio.getDocentesApoyo();
        if(docentePertenece(docentes,docente)) throw new RuntimeException("El docente tutor no puede ser docente de apoyo");
        this.emailService.enviarEmailTutor(docenteEmail, titulo, docente.getNombre() + " " + docente.getApellido(), docente.getArea());
    }
    public void eliminarTutor(String titulo){
        IdeaNegocio ideaNegocio = this.obtenerIdeaNegocio(titulo);
        if(ideaNegocio.getEstado().equals("aprobada"))
            throw new RuntimeException("No se puede modificar una idea de negocio aprobada");
        if(ideaNegocio.getTutor()==null)throw new RuntimeException("No hay un tutor Asignado, no se puede borrar");
        ideaNegocio.setTutor(null);
        this.ideaNegocioRepository.save(ideaNegocio);
    }
    public IdeaNegocio confirmarTutor(IdeaNegocio ideaNegocio) {
        return ideaNegocioRepository.save(ideaNegocio);
    }

    public Set<IdeaNegocio> listarIdeasDocenteApoyo(
            Integer docenteCodigo,
            Integer estudianteCodigo,
            Integer area,
            String estado){
        Set<IdeaNegocio> ideasNegocios = this.ideaNegocioRepository.findByDocenteApoyoFiltros(docenteCodigo, estudianteCodigo, area, estado);
        for(IdeaNegocio ideaNegocio : ideasNegocios){
            ideaNegocio = this.obtenerIdeaNegocio(ideaNegocio.getTitulo());
        }
        return ideasNegocios;
    }

    public Set<IdeaNegocio> listarIdeasDocenteEvaluador(
            Integer docenteCodigo,
            Integer estudianteCodigo,
            Integer area,
            String estado,
            LocalDate fechaInicio,
            LocalDate fechaFin){
        Set<IdeaNegocio> ideasNegocios = this.ideaNegocioRepository.findByEvaluadorFiltros(docenteCodigo, estudianteCodigo, area, estado, fechaInicio, fechaFin);
        for(IdeaNegocio ideaNegocio : ideasNegocios){
            ideaNegocio = this.obtenerIdeaNegocio(ideaNegocio.getTitulo());
        }
        return ideasNegocios;
    }

    public List<IdeaNegocio> comprobarIdeaAprobada(String correoEstudiante) {
        if (correoEstudiante == null)
            throw new RuntimeException("No se envió un correo con el que comprobar la idea");
        Estudiante estudiante = this.estudianteServices.obtenerEstudiante(correoEstudiante);
        List<IdeaNegocio> resultado = this.ideaNegocioRepository.findByLiderAprobada(estudiante.getCodigo());
        resultado.addAll(this.ideaNegocioRepository.findByIntegranteAprobada(estudiante.getCodigo()));
        return resultado;
    }
    private Boolean docentePertenece(Set<DocenteApoyoIdea> docentes ,Docente docente){
        Iterator<DocenteApoyoIdea> it = docentes.iterator();
        while (it.hasNext()){
            if(it.next().getDocente().equals(docente)) return true;
        }
        return  false;
    }
}
