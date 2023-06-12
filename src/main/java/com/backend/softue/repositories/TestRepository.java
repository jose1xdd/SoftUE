package com.backend.softue.repositories;

import com.backend.softue.models.Test;
import com.backend.softue.utils.response.ComponenteValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {

    @Query(value = "SELECT * FROM test WHERE codigo_estudiante = :codigo AND fecha_creacion = :fecha", nativeQuery = true)
    Test findByEstudianteAndFecha(@Param("codigo") Integer codigo, @Param("fecha") LocalDate fecha);

    @Query(value = "SELECT sum(suma.valor) FROM (SELECT sum(resultado.porcentaje) / count(*) AS valor FROM (SELECT (r.valor / mx) * cc.valor_porcentaje AS porcentaje, cc.id AS id FROM respuesta_seleccionada rs JOIN respuesta r ON(rs.respuesta_id = r.id) JOIN pregunta p ON(r.pregunta_id = p.id) JOIN componente_competencias cc ON(p.componente_competencias_id = cc.id) JOIN (SELECT p.id AS id, max(r.valor) AS mx FROM pregunta p JOIN respuesta r ON(p.id = r.pregunta_id) GROUP BY p.id) m ON(p.id = m.id) WHERE rs.test_id = :id_test) resultado GROUP BY resultado.id) AS suma", nativeQuery = true)
    Double obtenerResultado(@Param("id_test") Integer id);

    @Query(value = "SELECT resultado.nombre AS nombre, sum(resultado.porcentaje) / count(*) AS valor FROM (SELECT (r.valor / mx) * 100 AS porcentaje, cc.id AS id, cc.nombre AS nombre FROM respuesta_seleccionada rs JOIN respuesta r ON(rs.respuesta_id = r.id) JOIN pregunta p ON(r.pregunta_id = p.id) JOIN componente_competencias cc ON(p.componente_competencias_id = cc.id) JOIN (SELECT p.id AS id, max(r.valor) AS mx FROM pregunta p JOIN respuesta r ON(p.id = r.pregunta_id) GROUP BY p.id) m ON(p.id = m.id) WHERE rs.test_id = :id_test) resultado GROUP BY resultado.id", nativeQuery = true)
    List<ComponenteValue> obtenerResultadoComponentes(@Param("id_test") Integer id);

    @Query(value = "SELECT test.id FROM test WHERE test.codigo_estudiante = :codigo ORDER BY test.fecha_creacion, test.id DESC LIMIT 1", nativeQuery = true)
    Integer obtenerUltimoTestEstudiante(@Param("codigo") Integer codigo);

    @Query(value = "SELECT * FROM test", nativeQuery = true)
    List<Test> filtrarTest(@Param("codigoEstudiante") Integer codigo, @Param("cursoEstudiante") String curso, @Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);


}
