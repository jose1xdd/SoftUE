package com.backend.softue.repositories;
import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.models.PlanNegocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PlanNegocioRepository extends JpaRepository<PlanNegocio, Integer> {
    Optional<PlanNegocio> findByTitulo(String titulo);

    @Query(value = "SELECT plan.* FROM plan_negocio plan " +
            "LEFT JOIN docente d ON plan.tutor_codigo = d.codigo " +
            "LEFT JOIN usuario u_docente ON d.codigo = u_docente.codigo " +
            "LEFT JOIN estudiante e ON plan.codigo_estudiante_lider = e.codigo " +
            "LEFT JOIN usuario u_estudiante ON e.codigo = u_estudiante.codigo " +
            "LEFT JOIN area_conocimiento a ON a.id = plan.area_enfoque " +
            "WHERE " +
            "(:area IS NULL OR a.nombre = :area) " +
            "AND (u_docente.correo = :docenteEmail OR :docenteEmail IS NULL) " +
            "AND (:estudianteEmail IS NULL OR u_estudiante.correo = :estudianteEmail) " +
            "AND (:estado IS NULL OR plan.estado = :estado) " +
            "AND (:fechaInicio IS NULL OR plan.fecha_creacion >= :fechaInicio) " +
            "AND (:fechaFin IS NULL OR plan.fecha_creacion <= :fechaFin)", nativeQuery = true)
    List<PlanNegocio> findByFilters(
            @Param("docenteEmail") String docenteEmail,
            @Param("estudianteEmail") String estudianteEmail,
            @Param("area") String area,
            @Param("estado") String estado,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    @Query(value = "SELECT DISTINCT plan.* FROM plan_negocio plan " +
            "JOIN docente_apoyo_plan dap ON (idea.id = dap.plan_negocio_id)" +
            "JOIN plan_presentado pp ON (idea.id = pp.plan_negocio_id)" +
            "WHERE " +
            "(dap.docente_codigo = :docente_codigo OR :docente_codigo IS NULL) AND" +
            "(pp.estudiante_codigo = :estudiante_codigo OR idea.codigo_estudiante_lider = :estudiante_codigo OR  :estudiante_codigo IS NULL) AND" +
            "(plan.area_enfoque = :area OR :area IS NULL) AND" +
            "(plan.estado = :estado OR :estado IS NULL)"
            , nativeQuery = true)
    Set<PlanNegocio> findByDocenteApoyoFiltros(
            @Param("docente_codigo") Integer id,
            @Param("estudiante_codigo") Integer estudiante_codigo,
            @Param("area") Integer area,
            @Param("estado") String estado
    );

    @Query(value = "SELECT DISTINCT plan.* FROM plan_negocio plan " +
            "JOIN evaluacion_plan ep ON (plan.id = ep.plan_negocio) " +
            "JOIN calificacion_plan cp ON (ep.id = cp.evaluacion_plan_id) " +
            "JOIN plan_presentado pp ON (plan.id = pp.plan_negocio_id)" +
            "WHERE" +
            "(cp.Docente_codigo = :docente_codigo OR :docente_codigo IS NULL) AND" +
            "(pp.estudiante_codigo = :estudiante_codigo OR plan.codigo_estudiante_lider = :estudiante_codigo OR  :estudiante_codigo IS NULL) AND" +
            "(plan.area_enfoque = :area OR :area IS NULL) AND" +
            "(plan.estado = :estado OR :estado IS NULL) AND" +
            "((:fecha_inicio IS NULL AND :fecha_fin IS NULL) OR (ep.fecha_corte >= :fecha_inicio AND ep.fecha_corte <= :fecha_fin)) AND " +
            "(cp.estado = 'pendiente')"
            , nativeQuery = true)
    Set<PlanNegocio> findByEvaluadorFiltros(
            @Param("docente_codigo") Integer id,
            @Param("estudiante_codigo") Integer estudiante_codigo,
            @Param("area") Integer area,
            @Param("estado") String estado,
            @Param("fecha_inicio") LocalDate fecha_inicio,
            @Param("fecha_fin") LocalDate fecha_fin

    );
}
