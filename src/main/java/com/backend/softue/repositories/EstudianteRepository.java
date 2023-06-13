package com.backend.softue.repositories;

import com.backend.softue.models.Estudiante;
import com.backend.softue.models.IdeaNegocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {

    Estudiante findByCorreo(String correo);
    List<Estudiante> findByCurso(String curso);

    @Query(value = "SELECT distinct curso FROM estudiante", nativeQuery = true)
    Set<String> findCursos();

}
