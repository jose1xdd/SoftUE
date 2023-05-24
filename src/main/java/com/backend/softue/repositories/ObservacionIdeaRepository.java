package com.backend.softue.repositories;

import com.backend.softue.models.Docente;
import com.backend.softue.models.ObservacionIdea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObservacionIdeaRepository extends JpaRepository<ObservacionIdea, Integer> {
}
