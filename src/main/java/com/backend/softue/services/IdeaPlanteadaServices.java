package com.backend.softue.services;

import com.backend.softue.models.Estudiante;
import com.backend.softue.models.EstudianteIdeaKey;
import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.models.IdeaPlanteada;
import com.backend.softue.repositories.IdeaPlanteadaRepository;
import com.backend.softue.security.Hashing;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Setter
@Service
public class IdeaPlanteadaServices {

    @Autowired
    private IdeaPlanteadaRepository ideaPlanteadaRepository;

    private IdeaNegocioServices ideaNegocioServices;

    @Autowired
    private EstudianteServices estudianteServices;

    @Autowired
    private Hashing encrypt;

    public void agregarIntegrantes(IdeaNegocio ideaNegocio, List<Estudiante> estudiantesIntegrantes) {
        for (Estudiante estudiante : estudiantesIntegrantes) {
            agregarIntegrante(ideaNegocio, estudiante);
        }
    }

    public void agregarIntegrante(String JWT, String titulo, String correo) {
        IdeaNegocio ideaNegocio = null;
        Estudiante estudiante = null;
        try {
            estudiante = this.estudianteServices.obtenerEstudiante(correo);
        }
        catch (Exception e) {
            throw new RuntimeException("El estudiante no existe en la base de datos");
        }
        ideaNegocio = this.ideaNegocioServices.obtenerIdeaNegocio(titulo);

        if(ideaNegocio.getTutor() == null)
            throw new RuntimeException("La idea de negocio a eliminar no tiene un tutor asignado");
        if(!this.encrypt.getJwt().getKey(JWT).equals(ideaNegocio.getTutor().getCorreo()))
            throw new RuntimeException("Solo el tutor de una idea de negocio puede gestionar los integrantes de la misma");

        agregarIntegrante(ideaNegocio, estudiante);
    }

    public void agregarIntegrante(IdeaNegocio ideaNegocio, Estudiante estudiante) {
        try {
            System.out.println(ideaNegocio.getTitulo());
            ideaNegocio = this.ideaNegocioServices.obtenerIdeaNegocio(ideaNegocio.getTitulo());
        }
        catch (Exception e) {
            throw new RuntimeException("La idea de negocio no existe en la base de datos");
        }
        try {
            estudiante = this.estudianteServices.obtenerEstudiante(estudiante.getCorreo());
        }
        catch (Exception e) {
            throw new RuntimeException("El estudiante no existe en la base de datos");
        }
        this.ideaPlanteadaRepository.save(new IdeaPlanteada(new EstudianteIdeaKey(estudiante.getCodigo(), ideaNegocio.getId()), estudiante, ideaNegocio));
    }

    public void eliminarIntegrante(String JWT, String titulo, String correo) {
        IdeaNegocio ideaNegocio = null;
        Estudiante estudiante = null;
        try {
            ideaNegocio = this.ideaNegocioServices.obtenerIdeaNegocio(titulo);
        }
        catch (Exception e) {
            throw new RuntimeException("La idea de negocio no existe en la base de datos");
        }
        if(ideaNegocio.getTutor() == null)
            throw new RuntimeException("La idea de negocio a eliminar no tiene un tutor asignado");
        if(!this.encrypt.getJwt().getKey(JWT).equals(ideaNegocio.getTutor().getCorreo()))
            throw new RuntimeException("Solo el tutor de una idea de negocio puede gestionar los integrantes de la misma");

        try {
            estudiante = this.estudianteServices.obtenerEstudiante(correo);
        }
        catch (Exception e) {
            throw new RuntimeException("El estudiante no existe en la base de datos");
        }

        IdeaPlanteada ideaPlanteada = new IdeaPlanteada(new EstudianteIdeaKey(estudiante.getCodigo(), ideaNegocio.getId()), estudiante, ideaNegocio);
        Optional<IdeaPlanteada> resultado = this.ideaPlanteadaRepository.findById(ideaPlanteada.getId());

        if(!resultado.isPresent())
            throw new RuntimeException("El estudiante a eliminar no es integrante de la idea negocio");

        this.ideaPlanteadaRepository.delete(resultado.get());
    }
}
