package com.backend.softue.repositories;

import com.backend.softue.models.EvaluacionIdea;
import com.backend.softue.models.EvaluacionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluacionPlanRepository extends JpaRepository<EvaluacionPlan, Integer> {
    @Query(value =  "SELECT e.* " +
            "FROM evaluacion_plan e JOIN plan_negocio p ON(e.plan_negocio = p.id) " +
            "WHERE p.titulo = :titulo " +
            "ORDER BY e.fecha_presentacion, e.id DESC LIMIT 1", nativeQuery = true)
    Optional<EvaluacionPlan> evaluacionReciente(@Param("titulo") String titulo);

    @Query(value =  "SELECT * " +
            "FROM evaluacion_plan " +
            "WHERE plan_negocio = :id_plan " +
            "ORDER BY e.fecha_presentacion, e.id DESC", nativeQuery = true)
    List<EvaluacionPlan> findByPlanNegocio(@Param("id_plan") Integer plan);
}
