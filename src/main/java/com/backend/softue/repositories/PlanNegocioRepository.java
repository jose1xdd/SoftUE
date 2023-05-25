package com.backend.softue.repositories;
import com.backend.softue.models.PlanNegocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanNegocioRepository extends JpaRepository<PlanNegocio, Integer> {
    PlanNegocio findByTitulo(String titulo);

}
