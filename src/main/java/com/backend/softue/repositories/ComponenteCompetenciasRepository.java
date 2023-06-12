package com.backend.softue.repositories;

import com.backend.softue.models.ComponenteCompetencias;
import com.backend.softue.models.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComponenteCompetenciasRepository extends JpaRepository<ComponenteCompetencias, Integer> {
    ComponenteCompetencias findByNombre(String nombre);

    List<ComponenteCompetencias> findByEliminada(Boolean eliminada);

    @Query(value = "SELECT p.id FROM componente_competencias cc JOIN pregunta p ON(cc.id = p.componente_competencias_id) WHERE cc.id = :cc_id AND p.eliminada = false", nativeQuery = true)
    List<Integer> obtenerPreguntasByComponente(@Param(value = "cc_id") Integer id);
}
