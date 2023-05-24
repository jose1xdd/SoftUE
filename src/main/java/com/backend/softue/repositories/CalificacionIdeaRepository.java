package com.backend.softue.repositories;

import com.backend.softue.models.CalificacionIdea;
import com.backend.softue.models.CalificacionIdeaKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalificacionIdeaRepository extends JpaRepository<CalificacionIdea, CalificacionIdeaKey> {

    @Query(value =  "SELECT * " +
                    "FROM calificacion_idea " +
                    "WHERE evaluacion_idea_id = :id_evaluacion", nativeQuery = true)
    List<CalificacionIdea> findByEvaluacion(@Param("id_evaluacion") Integer idEvaluacion);
}
