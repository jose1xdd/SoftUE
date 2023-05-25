package com.backend.softue.repositories;

import com.backend.softue.models.PlanNegocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanNegocioRepository extends JpaRepository<PlanNegocio, Integer> {
    Optional<PlanNegocio> findByTitulo(String titulo);

}
