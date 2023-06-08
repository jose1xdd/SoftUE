package com.backend.softue.repositories;

import com.backend.softue.models.ComponenteCompetencias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponenteCompetenciasRepository extends JpaRepository<ComponenteCompetencias, Integer> {
    ComponenteCompetencias findByNombre(String nombre);
}
