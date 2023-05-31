package com.backend.softue.repositories;

import com.backend.softue.models.CalificacionPlan;
import com.backend.softue.models.CalificacionPlanKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalificacionPlanRepository extends JpaRepository<CalificacionPlan, CalificacionPlanKey> {
    @Query(value =  "SELECT * " +
            "FROM calificacion_plan " +
            "WHERE evaluacion_plan_id = :id_evaluacion", nativeQuery = true)
    List<CalificacionPlan> findByEvaluacion(@Param("id_evaluacion") Integer idEvaluacion);

}
