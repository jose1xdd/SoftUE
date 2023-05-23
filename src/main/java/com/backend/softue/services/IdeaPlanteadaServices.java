package com.backend.softue.services;

import com.backend.softue.models.Estudiante;
import com.backend.softue.models.EstudianteIdeaKey;
import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.models.IdeaPlanteada;
import com.backend.softue.repositories.EstudianteRepository;
import com.backend.softue.repositories.IdeaNegocioRepository;
import com.backend.softue.repositories.IdeaPlanteadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IdeaPlanteadaServices {

    @Autowired
    private IdeaNegocioRepository ideaNegocioRepository;

    @Autowired
    private IdeaPlanteadaRepository ideaPlanteadaRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    public void crear(IdeaNegocio ideaNegocio) {
        int numeroEstudiantes = 0;
        Estudiante estudiante = null;
        EstudianteIdeaKey id = null;
        IdeaPlanteada ideaPlanteada = null;
        String correo = "";

        IdeaNegocio resultado = this.ideaNegocioRepository.findByTitulo(ideaNegocio.getTitulo());
        if(resultado == null) throw new RuntimeException("No existe la idea de negocio a la cuál se le desea agregar integrantes estudiantes");

        integrantesExistentes(ideaNegocio);

        numeroEstudiantes = ideaNegocio.getCorreoEstudiantesIntegrantes().length;
        for(int i = 0; i < numeroEstudiantes; i++) {
            correo = ideaNegocio.getCorreoEstudiantesIntegrantes()[i];
            estudiante = this.estudianteRepository.findByCorreo(correo);
            id = new EstudianteIdeaKey(estudiante.getCodigo(), resultado.getId());
            ideaPlanteada = new IdeaPlanteada(id, estudiante, resultado);
            this.ideaPlanteadaRepository.save(ideaPlanteada);
        }

    }

    public void integrantesExistentes(IdeaNegocio ideaNegocio){
        int numeroEstudiantes = 0;
        String correo = "";
        Estudiante estudiante = null;

        numeroEstudiantes = ideaNegocio.getCorreoEstudiantesIntegrantes().length;
        for(int i = 0; i < numeroEstudiantes; i++) {
            correo = ideaNegocio.getCorreoEstudiantesIntegrantes()[i];
            estudiante = this.estudianteRepository.findByCorreo(correo);
            if (estudiante == null)
                throw new RuntimeException("El estudiante integrante que tiene como correo " + correo + " no existe");
        }
    }

    public void eliminar(IdeaNegocio ideaNegocio, Estudiante estudiante) {
        IdeaNegocio resultado = this.ideaNegocioRepository.findByTitulo(ideaNegocio.getTitulo());
        if(resultado == null)
            throw new RuntimeException("No existe la idea de negocio a la cuál se le desea eliminar un integrante estudiante");
        Estudiante estudianteEliminar = this.estudianteRepository.findByCorreo(estudiante.getCorreo());
        EstudianteIdeaKey estudianteIdeaKey = new EstudianteIdeaKey(estudianteEliminar.getCodigo(), resultado.getId());
        IdeaPlanteada ideaPlanteadaEliminar = this.ideaPlanteadaRepository.findById(estudianteIdeaKey).get();

        if(ideaPlanteadaEliminar != null)
            this.ideaPlanteadaRepository.delete(ideaPlanteadaEliminar);
    }
}
