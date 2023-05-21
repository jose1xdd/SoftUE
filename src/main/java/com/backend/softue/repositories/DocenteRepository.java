package com.backend.softue.repositories;

import com.backend.softue.models.Docente;
import com.backend.softue.models.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocenteRepository extends JpaRepository<Docente, Integer> {
}
