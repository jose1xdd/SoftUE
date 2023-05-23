package com.backend.softue.repositories;

import com.backend.softue.models.DocenteApoyoIdea;
import com.backend.softue.models.DocenteIdeaKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocenteApoyoIdeaRepository extends JpaRepository<DocenteApoyoIdea, DocenteIdeaKey> {

}
