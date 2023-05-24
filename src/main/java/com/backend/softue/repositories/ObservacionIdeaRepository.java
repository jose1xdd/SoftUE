package com.backend.softue.repositories;


import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.models.ObservacionIdea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ObservacionIdeaRepository extends JpaRepository<ObservacionIdea, Integer> {
    Set<ObservacionIdea> findByIdeaNegocioId(IdeaNegocio ideaNegocio);
}
