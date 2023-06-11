package com.backend.softue.repositories;

import com.backend.softue.models.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {

    @Query(value = "SELECT * FROM test WHERE codigo_estudiante = :codigo AND fecha_creacion = :fecha", nativeQuery = true)
    Test findByEstudianteAndFecha(@Param("codigo") Integer codigo, @Param("fecha") LocalDate fecha);

    @Query(value = "SELECT sum(resultado.porcentaje) FROM " +
            "(SELECT (r.valor / mx) * cc.valor_porcentaje AS porcentaje FROM respuesta_seleccionada rs JOIN respuesta r ON(rs.respuesta_id = r.id) " +
            "JOIN pregunta p ON(r.pregunta_id = p.id) " +
            "JOIN componente_competencias cc ON(p.componente_competencias_id = cc.id) " +
            "JOIN (SELECT p.id AS id, max(r.valor) AS mx FROM pregunta p JOIN respuesta r ON(p.id = r.pregunta_id) GROUP BY p.id) m " +
            "ON(p.id = m.id) " +
            "WHERE rs.test_id = :id_test) resultado", nativeQuery = true)
    Double obtenerResultado(@Param("id_test") Integer id);
}
