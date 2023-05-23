package com.backend.softue.repositories;

import com.backend.softue.models.DocumentoIdea;
import com.backend.softue.models.IdeaNegocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoIdeaRepository extends JpaRepository<DocumentoIdea, Integer> {

}
