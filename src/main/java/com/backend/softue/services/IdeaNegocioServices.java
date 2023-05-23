package com.backend.softue.services;

import com.backend.softue.models.Estudiante;
import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.repositories.EstudianteRepository;
import com.backend.softue.repositories.IdeaNegocioRepository;
import com.backend.softue.security.Hashing;
import com.backend.softue.security.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IdeaNegocioServices {

    @Autowired
    private IdeaNegocioRepository ideaNegocioRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private IdeaPlanteadaServices ideaPlanteadaServices;

    @Autowired
    private EstudianteServices estudianteServices;

    @Autowired
    private Hashing encrypt;

    @Autowired
    private Roles roles;

    public void crear(IdeaNegocio ideaNegocio, String JWT) {
        String correo;
        List<String> estudiantesRepetidos;
        IdeaNegocio resultado = this.ideaNegocioRepository.findByTitulo(ideaNegocio.getTitulo());
        if(resultado != null)
            throw new RuntimeException("Existe otra idea de negocio con el mismo título");
        if(!this.encrypt.getJwt().getValue(JWT).toLowerCase().equals("estudiante"))
            throw new RuntimeException("No se puede crear una idea de negocio si no se es un estudiante");

        if(ideaNegocio.getCorreoEstudiantesIntegrantes() != null) {
            correo = this.estudianteServices.estudiantesExisten(ideaNegocio.getCorreoEstudiantesIntegrantes());
            if (!correo.equals(""))
                throw new RuntimeException("El estudiante integrante que tiene como correo " + correo + " no existe");
            correo = this.encrypt.getJwt().getKey(JWT);
            //Devuelve un error si encuentra estudiantes repetidos
            this.estudianteServices.estudiantesRepetidos(ideaNegocio.getCorreoEstudiantesIntegrantes(), new String[]{correo});
        }

        correo = this.encrypt.getJwt().getKey(JWT);
        Estudiante estudiante = this.estudianteRepository.findByCorreo(correo);
        ideaNegocio.setEstudianteLider(estudiante);

        this.ideaNegocioRepository.save(ideaNegocio);
    }

    public void actualizarTitulo(IdeaNegocio ideaNegocio, String JWT) {
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
        String correo = null;
        if(ideaNegocio == null)
            throw new RuntimeException("No existe la idea de negocio a la cuál se le desea agregar un integrante estudiante");

        if(ideaNegocio.getTutor() != null)
            correo = ideaNegocio.getTutor().getCorreo();
        if(!this.encrypt.getJwt().getKey(JWT).equals(correo))
            throw new RuntimeException("Solo el docente tutor de la idea de negocio puede agregar un integrantes.");

        if(estudiante.getCorreo().equals(ideaNegocio.getEstudianteLider().getCorreo()))
            throw new RuntimeException("El estudiante líder no puede ser estudiante de apoyo en la idea de negocio");

        ideaNegocio.setCorreoEstudiantesIntegrantes(new String[] {estudiante.getCorreo()});
        this.ideaPlanteadaServices.crear(ideaNegocio);
    }

    public void eliminarIntegrante(Estudiante estudiante, String titulo, String JWT){
        IdeaNegocio ideaNegocio = this.ideaNegocioRepository.findByTitulo(titulo);
        String correo = null;
        if(ideaNegocio == null)
            throw new RuntimeException("No existe la idea de negocio a la cuál se le desea eliminar un integrante estudiante");
        if(ideaNegocio.getTutor() != null)
            correo = ideaNegocio.getTutor().getCorreo();
        if(!this.encrypt.getJwt().getKey(JWT).equals(correo))
            throw new RuntimeException("Solo el docente tutor de la idea de negocio puede eliminar integrantes.");

        this.ideaPlanteadaServices.eliminar(ideaNegocio, estudiante);
    }

}
