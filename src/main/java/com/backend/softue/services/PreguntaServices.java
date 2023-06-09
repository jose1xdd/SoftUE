package com.backend.softue.services;

import com.backend.softue.models.ComponenteCompetencias;
import com.backend.softue.models.Pregunta;
import com.backend.softue.repositories.PreguntaRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Getter
@Service
public class PreguntaServices {

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private ComponenteCompetenciasServices componenteCompetenciasServices;

    @Autowired
    private RespuestaServices respuestaServices;

    @PostConstruct
    public void init() {
        this.respuestaServices.setPreguntaServices(this);
    }

    public void crear(String enunciado, String nombreComponente) {
        if (enunciado == null || enunciado.equals(""))
            throw new RuntimeException("El enunciado no puede estar vacío");
        ComponenteCompetencias componenteCompetencias = this.componenteCompetenciasServices.obtener(nombreComponente);
        this.preguntaRepository.save(new Pregunta(null, enunciado, componenteCompetencias, null, null));
    }

    public List<Pregunta> listar() {
        List<Pregunta> resultado = this.preguntaRepository.findAll();
        for (Pregunta pregunta : resultado) {
            pregunta.setListaRespuestas(this.respuestaServices.obtenerRespuestas(pregunta.getId()));
        }
        return resultado;
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

    public Pregunta obtener(Integer id) {
        if (id == null)
            throw new RuntimeException("No se tiene un id con el que buscar la pregunta");
        Optional<Pregunta> resultado = this.preguntaRepository.findById(id);
        if (!resultado.isPresent())
            throw new RuntimeException("La pregunta a buscar no existe");
        Pregunta pregunta = resultado.get();
        pregunta.setListaRespuestas(this.respuestaServices.obtenerRespuestas(pregunta));
        return pregunta;
    }
}
