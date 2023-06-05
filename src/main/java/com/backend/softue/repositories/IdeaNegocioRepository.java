package com.backend.softue.repositories;

import com.backend.softue.models.IdeaNegocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface IdeaNegocioRepository extends JpaRepository<IdeaNegocio, Integer> {
    IdeaNegocio findByTitulo(String titulo);

    @Query(value = "SELECT idea.* FROM idea_negocio idea " +
            "LEFT JOIN docente d ON idea.tutor_codigo = d.codigo " +
            "LEFT JOIN usuario u_docente ON d.codigo = u_docente.codigo " +
            "LEFT JOIN estudiante e ON idea.codigo_estudiante_lider = e.codigo " +
            "LEFT JOIN usuario u_estudiante ON e.codigo = u_estudiante.codigo " +
            "WHERE " +
            "(:minera IS NULL OR idea.area_enfoque = :minera) " +
            "AND (u_docente.correo = :docenteEmail OR :docenteEmail IS NULL) " +
            "AND (:estudianteEmail IS NULL OR u_estudiante.correo = :estudianteEmail) " +
            "AND (:estado IS NULL OR idea.estado = :estado) " +
            "AND (:fechaInicio IS NULL OR idea.fecha_creacion >= :fechaInicio) " +
            "AND (:fechaFin IS NULL OR idea.fecha_creacion <= :fechaFin)", nativeQuery = true)
    List<IdeaNegocio> findByFilters(
            @Param("docenteEmail") String docenteEmail,
            @Param("estudianteEmail") String estudianteEmail,
            @Param("minera") String minera,
            @Param("estado") String estado,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    @Query(value = "SELECT idea.* FROM idea_negocio idea " +
                   "JOIN Evaluacion_idea ei ON (idea.id = ei.ideaNegocio_id)" +
                   "JOIN Calificacion_idea ci ON (ei.id = ci.Evaluacion_idea)" +
                   "WHERE Docente_codigo = :id", nativeQuery = true)
    Set<IdeaNegocio> findByEvaluador(@Param("docente_codigo") Integer id);




}
