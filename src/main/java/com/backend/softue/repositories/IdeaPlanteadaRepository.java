package com.backend.softue.repositories;

import com.backend.softue.models.EstudianteIdeaKey;
import com.backend.softue.models.IdeaPlanteada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdeaPlanteadaRepository extends JpaRepository<IdeaPlanteada, EstudianteIdeaKey> {
}
