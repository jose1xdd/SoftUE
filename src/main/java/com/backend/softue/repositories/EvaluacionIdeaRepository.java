package com.backend.softue.repositories;

import com.backend.softue.models.EvaluacionIdea;
import com.backend.softue.models.IdeaNegocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluacionIdeaRepository extends JpaRepository<EvaluacionIdea, Integer> {

    @Query(value =  "SELECT e.* " +
                    "FROM evaluacion_idea e JOIN idea_negocio i ON(e.idea_negocio = i.id) " +
                    "WHERE i.titulo = :titulo " +
                    "ORDER BY e.fecha_presentacion, e.id DESC LIMIT 1", nativeQuery = true)
    Optional<EvaluacionIdea> evaluacionReciente(@Param("titulo") String titulo);

    @Query(value =  "SELECT * " +
            "FROM evaluacion_idea " +
            "WHERE idea_negocio = :id_idea ", nativeQuery = true)
    List<EvaluacionIdea> findByIdeaNegocio(@Param("id_idea") Integer idea);
}
