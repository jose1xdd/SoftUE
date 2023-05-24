package com.backend.softue.repositories;

import com.backend.softue.models.EvaluacionIdea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluacionIdeaRepository extends JpaRepository<EvaluacionIdea, Integer> {

}
