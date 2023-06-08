package com.backend.softue.services;

import com.backend.softue.models.ComponenteCompetencias;
import com.backend.softue.models.Pregunta;
import com.backend.softue.repositories.PreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class PreguntaServices {

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private ComponenteCompetenciasServices componenteCompetenciasServices;

    public void crear(String enunciado, String nombreComponente) {
        if (enunciado == null || enunciado.equals(""))
            throw new RuntimeException("El enunciado no puede estar vacío");
        ComponenteCompetencias componenteCompetencias = this.componenteCompetenciasServices.obtener(nombreComponente);
        this.preguntaRepository.save(new Pregunta(null, enunciado, componenteCompetencias));
    }

    public List<Pregunta> listar() {
        return this.preguntaRepository.findAll();
    }

    public void actualizar(String id, String enunciado, String nombreComponente) {
        if (id == null)
            throw new RuntimeException("El id de la que se desea actualizar no existe");
        if (enunciado.equals(""))
            throw new RuntimeException("El enunciado no puede estar vacío");
        Pregunta pregunta = this.preguntaRepository.findById(Integer.parseInt(id)).get();
        if (pregunta == null)
            throw new RuntimeException("La pregunta con ese id no existe");
        if (enunciado != null)
            pregunta.setEnunciado(enunciado);
        if (nombreComponente != null) {
            ComponenteCompetencias componenteCompetencias = this.componenteCompetenciasServices.obtener(nombreComponente);
            if (componenteCompetencias == null)
                throw new RuntimeException("No existe un componente con ese nombre");
            pregunta.setComponenteCompetenciasId(componenteCompetencias);
        }
        this.preguntaRepository.save(pregunta);
    }

    public void eliminar(Integer id) {
        if (id == null)
            throw new RuntimeException("No se tiene un id con el que eliminar la pregunta");
        if (!this.preguntaRepository.existsById(id))
            throw new RuntimeException("La pregunta a eliminar no existe");
        this.preguntaRepository.deleteById(id);
    }
}
