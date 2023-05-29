package com.backend.softue.repositories;

import com.backend.softue.models.EstudiantePlanKey;
import com.backend.softue.models.PlanPresentado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanPresentadoRepository extends JpaRepository<PlanPresentado, EstudiantePlanKey> {
}
