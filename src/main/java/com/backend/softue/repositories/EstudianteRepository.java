package com.backend.softue.repositories;

import com.backend.softue.models.Estudiante;
import com.backend.softue.models.IdeaNegocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {

    Estudiante findByCorreo(String correo);

    @Query(value = "SELECT count(*) FROM estudiante WHERE estudiante.codigo_institucional = :codigo", nativeQuery = true)
    Integer findByCodigo(@Param("codigo") Long codigo);

    List<Estudiante> findByCurso(String curso);

    @Query(value = "SELECT distinct curso FROM Estudiante ", nativeQuery = true)
    Set<String> findCursos();

}
