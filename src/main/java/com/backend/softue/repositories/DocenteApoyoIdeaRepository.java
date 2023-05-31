package com.backend.softue.repositories;

import com.backend.softue.models.Docente;
import com.backend.softue.models.DocenteApoyoIdea;
import com.backend.softue.models.DocenteIdeaKey;
import com.backend.softue.models.IdeaNegocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocenteApoyoIdeaRepository extends JpaRepository<DocenteApoyoIdea, DocenteIdeaKey> {
}
