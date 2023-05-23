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

}
