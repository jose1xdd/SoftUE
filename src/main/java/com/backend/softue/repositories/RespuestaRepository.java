package com.backend.softue.repositories;

import com.backend.softue.models.Pregunta;
import com.backend.softue.models.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Integer> {
    List<Respuesta> findByPreguntaId(Pregunta preguntaId);
}
