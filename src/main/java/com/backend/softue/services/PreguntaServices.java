package com.backend.softue.services;

import com.backend.softue.models.ComponenteCompetencias;
import com.backend.softue.models.Pregunta;
import com.backend.softue.repositories.PreguntaRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Service
public class PreguntaServices {

    @Autowired
    private PreguntaRepository preguntaRepository;

    private ComponenteCompetenciasServices componenteCompetenciasServices;

    @Autowired
    private RespuestaServices respuestaServices;

    @PostConstruct
    public void init() {
        this.respuestaServices.setPreguntaServices(this);
    }

    public Pregunta crear(String enunciado, String nombreComponente) {
        if (enunciado == null || enunciado.equals(""))
            throw new RuntimeException("El enunciado no puede estar vacío");
        ComponenteCompetencias componenteCompetencias = this.componenteCompetenciasServices.obtener(nombreComponente);
        return this.preguntaRepository.save(new Pregunta(null, enunciado, false, componenteCompetencias, null, null));
    }

    public List<Pregunta> listar() {
        List<Pregunta> resultado = this.preguntaRepository.findByEliminada(false);
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
        if (pregunta == null || pregunta.getEliminada())
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
        Pregunta pregunta = this.preguntaRepository.findById(id).get();
        if (pregunta == null || pregunta.getEliminada())
            throw new RuntimeException("La pregunta a eliminar no existe");
        pregunta.setEliminada(true);
        this.preguntaRepository.save(pregunta);
    }

    public Pregunta obtener(Integer id) {
        if (id == null)
            throw new RuntimeException("No se tiene un id con el que buscar la pregunta");
        Optional<Pregunta> resultado = this.preguntaRepository.findById(id);
        if (!resultado.isPresent() || resultado.get().getEliminada())
            throw new RuntimeException("La pregunta a buscar no existe");
        Pregunta pregunta = resultado.get();
        pregunta.setListaRespuestas(this.respuestaServices.obtenerRespuestas(pregunta));
        return pregunta;
    }
}
