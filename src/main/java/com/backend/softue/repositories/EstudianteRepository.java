package com.backend.softue.repositories;

import com.backend.softue.models.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {

    Estudiante findByCorreo(String correo);
    List<Estudiante> findByCurso(String curso);

}
