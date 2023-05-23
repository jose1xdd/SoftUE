package com.backend.softue.services;

import com.backend.softue.models.Estudiante;
import com.backend.softue.models.EstudianteIdeaKey;
import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.models.IdeaPlanteada;
import com.backend.softue.repositories.EstudianteRepository;
import com.backend.softue.repositories.IdeaNegocioRepository;
import com.backend.softue.repositories.IdeaPlanteadaRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Setter
@Service
public class IdeaPlanteadaServices {

    @Autowired
    private IdeaPlanteadaRepository ideaPlanteadaRepository;

    private IdeaNegocioServices ideaNegocioServices;

    @Autowired
    private EstudianteServices estudianteServices;

    public void agregarIntegrantes(IdeaNegocio ideaNegocio, List<Estudiante> estudiantesIntegrantes) {
        for (Estudiante estudiante : estudiantesIntegrantes) {
            agregarIntegrante(ideaNegocio, estudiante);
        }
    }

    public void agregarIntegrante(IdeaNegocio ideaNegocio, Estudiante estudiante) {
        try {
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

    /*public void integrantesExistentes(IdeaNegocio ideaNegocio){
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
    }*/
}
