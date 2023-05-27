package com.backend.softue.repositories;

import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.models.ObservacionIdea;
import com.backend.softue.models.ObservacionPlan;
import com.backend.softue.models.PlanNegocio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ObservacionPlanRepository extends JpaRepository<ObservacionPlan, Integer> {
    Set<ObservacionPlan> findByPlanNegocioId(PlanNegocio planNegocio);
}
