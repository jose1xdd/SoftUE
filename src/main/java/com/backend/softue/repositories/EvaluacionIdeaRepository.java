package com.backend.softue.repositories;

import com.backend.softue.models.EvaluacionIdea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EvaluacionIdeaRepository extends JpaRepository<EvaluacionIdea, Integer> {

    @Query(value =  "SELECT e.* " +
                    "FROM evaluacion_idea e JOIN idea_negocio i ON(e.idea_negocio = i.id) " +
                    "WHERE i.titulo = :titulo " +
                    "ORDER BY e.fecha_presentacion DESC LIMIT 1", nativeQuery = true)
    Optional<EvaluacionIdea> evaluacionReciente(@Param("titulo") String titulo);
}
