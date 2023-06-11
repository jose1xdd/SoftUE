package com.backend.softue.services;

import com.backend.softue.models.Pregunta;
import com.backend.softue.models.Respuesta;
import com.backend.softue.repositories.RespuestaRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Setter
@Service
public class RespuestaServices {

    @Autowired
    private RespuestaRepository respuestaRepository;


    private PreguntaServices preguntaServices;

    public void crear(String contenido, Integer valor, Integer preguntaId) {
        if (contenido == null || contenido.equals(""))
            throw new RuntimeException("El contenido no puede estar vacío");
        if (valor == null)
            throw new RuntimeException("El valor de la respuesta no puede ser nulo");
        if (!(0 <= valor && valor <= 100))
            throw new RuntimeException("El valor debe estar entre 0 y 100");
        Pregunta pregunta = this.preguntaServices.obtener(preguntaId);
        this.respuestaRepository.save(new Respuesta(null, contenido, valor, pregunta));
    }

    public void eliminar(Integer id) {
        if (id == null)
            throw new RuntimeException("No se tiene un id con el que eliminar la respuesta");
        if (!this.respuestaRepository.existsById(id))
            throw new RuntimeException("La respuesta a eliminar no existe");
        this.respuestaRepository.deleteById(id);
    }

    public void actualizar(Integer id, String contenido, Integer valor, Integer preguntaId) {
        if (id == null)
            throw new RuntimeException("El id con el que se desea buscar la respuesta no existe");
        if (contenido.equals(""))
            throw new RuntimeException("El contenido no puede estar vacío");
        Respuesta respuesta = this.respuestaRepository.findById(id).get();
        if (respuesta == null)
            throw new RuntimeException("La respuesta con ese id no existe");
        if (contenido != null)
            respuesta.setContenido(contenido);
        if (valor != null)
            respuesta.setValor(valor);
        if (preguntaId != null) {
            Pregunta pregunta = this.preguntaServices.obtener(preguntaId);
            if (pregunta == null)
                throw new RuntimeException("No existe una pregunta con ese nombre");
            respuesta.setPreguntaId(pregunta);
        }
        this.respuestaRepository.save(respuesta);
    }

    public Respuesta obtener(Integer id) {
        if (id == null)
            throw new RuntimeException("No se puede obtener una respuesta sin un ID");
        Respuesta respuesta = this.respuestaRepository.findById(id).get();
        if (respuesta == null)
            throw new RuntimeException("No existe una respuesta con ese ID");
        return respuesta;
    }

    public List<Respuesta> obtenerRespuestas(Integer id) {
        if (id == null)
            throw new RuntimeException("No se pueden obtener las respuestas de un id null");
        Pregunta pregunta = this.preguntaServices.obtener(id);
        if (pregunta == null || pregunta.getEliminada())
            throw new RuntimeException("La pregunta de las que se desea obtener las respuestas no existe");
        return this.respuestaRepository.findByPreguntaId(pregunta);
    }

    public List<Respuesta> obtenerRespuestas(Pregunta pregunta) {
        if (pregunta == null || pregunta.getEliminada())
            throw new RuntimeException("No se pueden obtener las respuestas de un null");
        return this.respuestaRepository.findByPreguntaId(pregunta);
    }
}
