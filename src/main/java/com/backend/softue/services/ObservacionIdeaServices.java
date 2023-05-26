package com.backend.softue.services;

import com.backend.softue.models.*;
import com.backend.softue.repositories.ObservacionIdeaRepository;
import com.backend.softue.security.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Service
public class ObservacionIdeaServices {

    @Autowired
    private DocenteServices docenteServices;
    @Autowired
    private IdeaNegocioServices ideaNegocioServices;

    @Autowired
    private DocenteApoyoIdeaServices docenteApoyoIdeaServices;

    @Autowired
    private ObservacionIdeaRepository observacionIdeaRepository;

    @Autowired
    private Hashing encrypt;
    public void crearObservacion(String jwt, String ideaTitulo, String observacion){
        String correoDocente = this.encrypt.getJwt().getKey(jwt);
        if(ideaTitulo == null)
            throw new RuntimeException("No se suministro un titulo de la idea de negocio");
        if(observacion == null)
            throw new RuntimeException("No se suministro una observacion");

        IdeaNegocio idea = this.ideaNegocioServices.obtenerIdeaNegocio(ideaTitulo);
        if(idea == null)
            throw new RuntimeException("No existe una idea de negocio asignada a ese titulo");

        Docente docente = this.docenteServices.obtenerDocente(correoDocente);
        if(docente == null)
            throw new RuntimeException("No existe un docente asignado a ese correo");

        if(!permisosCrear(idea,correoDocente))
            throw new RuntimeException("El docente no tiene permisos para realizar la observacion");

        this.observacionIdeaRepository.save(new ObservacionIdea(null, idea,null, docente,null, observacion, LocalDateTime.now()));
    }

    public Set<ObservacionIdea> obtenerObservaciones(String jwt, String titulo){
        if(titulo == null)
            throw new RuntimeException("No se suministro un titulo de idea de negocio");

        IdeaNegocio idea = this.ideaNegocioServices.obtenerIdeaNegocio(titulo);
        if(idea == null)
            throw new RuntimeException("No existe una idea de negocio con ese titulo");

        if(!this.permisosObtener(jwt, idea))
            throw new RuntimeException("No se cuentan con los permisos necesarios");

        Set<ObservacionIdea> result = this.observacionIdeaRepository.findByIdeaNegocioId(idea);
        if(result != null){
            for(ObservacionIdea v : result){
                v.setTitulo(v.getIdeaNegocioId().getTitulo());
                v.setDocenteInfo(new String[][]{{v.getDocenteId().getCorreo()},{v.getDocenteId().getNombre() + " " + v.getDocenteId().getApellido()}});
            }
        }
        return result;
    }

    private boolean permisosObtener(String jwt, IdeaNegocio idea){
        boolean permisos = false;
        String correo = this.encrypt.getJwt().getKey(jwt);
        String rol =  this.encrypt.getJwt().getValue(jwt);

        if(idea.getEstudianteLider().getCorreo().equals(correo)) permisos = true;
        if(!permisos && idea.getTutor().getCorreo().equals(correo)) permisos = true;
        if(!permisos && (rol.equals("administrativo") || rol.equals("coordinador"))) permisos = true;

        if(!permisos && idea.getDocentesApoyo() != null){
            for(DocenteApoyoIdea v : idea.getDocentesApoyo()){
                if(v.getDocente().getCorreo().equals(correo)) permisos = true;
            }
        }

        if(!permisos && idea.getEstudiantesIntegrantes() != null){
            for(IdeaPlanteada v : idea.getEstudiantesIntegrantes()){
                if(v.getEstudiante().equals(correo)) permisos = true;
            }
        }
        return permisos;
    }

    private boolean permisosCrear(IdeaNegocio idea, String correoDocente) {
        boolean permisos = false;
        if (idea.getDocentesApoyo() != null){
            for (DocenteApoyoIdea v : idea.getDocentesApoyo()) {
                if (v.getDocente().getCorreo().equals(correoDocente)) permisos = true;
            }
        }
    return permisos || idea.getTutor().getCorreo().equals(correoDocente);
    }
}
