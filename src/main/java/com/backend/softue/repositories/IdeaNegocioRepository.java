package com.backend.softue.repositories;

import com.backend.softue.models.Estudiante;
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

    List<IdeaNegocio> findByEstudianteLider(Estudiante estudianteLider);

    @Query(value = "SELECT idea.* FROM idea_negocio idea " +
            "LEFT JOIN docente d ON idea.tutor_codigo = d.codigo " +
            "LEFT JOIN usuario u_docente ON d.codigo = u_docente.codigo " +
            "LEFT JOIN estudiante e ON idea.codigo_estudiante_lider = e.codigo " +
            "LEFT JOIN usuario u_estudiante ON e.codigo = u_estudiante.codigo " +
            "LEFT JOIN area_conocimiento a ON a.id = idea.area_enfoque " +
            "WHERE " +
            "(:area IS NULL OR a.nombre = :area) " +
            "AND (u_docente.correo = :docenteEmail OR :docenteEmail IS NULL) " +
            "AND (:estudianteEmail IS NULL OR u_estudiante.correo = :estudianteEmail) " +
            "AND (:estado IS NULL OR idea.estado = :estado) " +
            "AND (:fechaInicio IS NULL OR idea.fecha_creacion >= :fechaInicio) " +
            "AND (:fechaFin IS NULL OR idea.fecha_creacion <= :fechaFin)", nativeQuery = true)
    List<IdeaNegocio> findByFilters(
            @Param("docenteEmail") String docenteEmail,
            @Param("estudianteEmail") String estudianteEmail,
            @Param("area") String area,
            @Param("estado") String estado,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    @Query(value = "SELECT DISTINCT idea.* FROM idea_negocio idea " +
            "JOIN docente_apoyo_idea dai ON (idea.id = dai.idea_negocio_id)" +
            "JOIN idea_planteada ip ON (idea.id = ip.idea_negocio_id)" +
            "WHERE " +
            "(dai.docente_codigo = :docente_codigo OR :docente_codigo IS NULL) AND" +
            "(ip.estudiante_codigo = :estudiante_codigo OR idea.codigo_estudiante_lider = :estudiante_codigo OR  :estudiante_codigo IS NULL) AND" +
            "(idea.area_enfoque = :area OR :area IS NULL) AND" +
            "(idea.estado = :estado OR :estado IS NULL)"
            , nativeQuery = true)
    Set<IdeaNegocio> findByDocenteApoyoFiltros(
            @Param("docente_codigo") Integer id,
            @Param("estudiante_codigo") Integer estudiante_codigo,
            @Param("area") Integer area,
            @Param("estado") String estado
    );


    @Query(value = "SELECT DISTINCT idea.* FROM idea_negocio idea " +
            "JOIN evaluacion_idea ei ON (idea.id = ei.idea_negocio) " +
            "JOIN calificacion_idea ci ON (ei.id = ci.evaluacion_idea_id) " +
            "JOIN idea_planteada ip ON (idea.id = ip.idea_negocio_id)" +
            "WHERE" +
            "(ci.Docente_codigo = :docente_codigo OR :docente_codigo IS NULL) AND" +
            "(ip.estudiante_codigo = :estudiante_codigo OR idea.codigo_estudiante_lider = :estudiante_codigo OR  :estudiante_codigo IS NULL) AND" +
            "(idea.area_enfoque = :area OR :area IS NULL) AND" +
            "(idea.estado = :estado OR :estado IS NULL) AND" +
            "((:fecha_inicio IS NULL AND :fecha_fin IS NULL) OR (ei.fecha_corte >= :fecha_inicio AND ei.fecha_corte <= :fecha_fin))"
            , nativeQuery = true)
    Set<IdeaNegocio> findByEvaluadorFiltros(
            @Param("docente_codigo") Integer id,
            @Param("estudiante_codigo") Integer estudiante_codigo,
            @Param("area") Integer area,
            @Param("estado") String estado,
            @Param("fecha_inicio") LocalDate fecha_inicio,
            @Param("fecha_fin") LocalDate fecha_fin

    );

    Set<IdeaNegocio> findByDocentesApoyo(@Param("docente_codigo") Integer id);

    @Query(value = "SELECT idea.* FROM idea_negocio idea " +
            "JOIN idea_planteada idea_p ON (idea.id = idea_p.idea_negocio_id)" +
            "JOIN estudiante e ON (idea_p.estudiante_codigo = e.codigo)" +
            "WHERE e.codigo = :codigo_estudiante AND idea.estado = 'aprobada'", nativeQuery = true)
    List<IdeaNegocio> findByIntegranteAprobada(@Param("codigo_estudiante") Integer codigo);

    @Query(value = "SELECT idea.* FROM idea_negocio idea " +
            "JOIN estudiante e ON (idea.codigo_estudiante_lider = e.codigo)" +
            "WHERE e.codigo = :codigo_estudiante AND idea.estado = 'aprobada'", nativeQuery = true)
    List<IdeaNegocio> findByLiderAprobada(@Param("codigo_estudiante") Integer codigo);
}
