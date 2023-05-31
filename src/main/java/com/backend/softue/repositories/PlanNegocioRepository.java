package com.backend.softue.repositories;
import com.backend.softue.models.PlanNegocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlanNegocioRepository extends JpaRepository<PlanNegocio, Integer> {
    Optional<PlanNegocio> findByTitulo(String titulo);

    @Query(value = "SELECT plan.* FROM plan_negocio plan " +
            "LEFT JOIN docente d ON plan.tutor_codigo = d.codigo " +
            "LEFT JOIN usuario u_docente ON d.codigo = u_docente.codigo " +
            "LEFT JOIN estudiante e ON plan.codigo_estudiante_lider = e.codigo " +
            "LEFT JOIN usuario u_estudiante ON e.codigo = u_estudiante.codigo " +
            "WHERE " +
            "(:minera IS NULL OR plan.area_enfoque = :minera) " +
            "AND (u_docente.correo = :docenteEmail OR :docenteEmail IS NULL) " +
            "AND (:estudianteEmail IS NULL OR u_estudiante.correo = :estudianteEmail) " +
            "AND (:estado IS NULL OR plan.estado = :estado) " +
            "AND (:fechaInicio IS NULL OR plan.fecha_creacion >= :fechaInicio) " +
            "AND (:fechaFin IS NULL OR plan.fecha_creacion <= :fechaFin)", nativeQuery = true)
    List<PlanNegocio> findByFilters(
            @Param("docenteEmail") String docenteEmail,
            @Param("estudianteEmail") String estudianteEmail,
            @Param("minera") String minera,
            @Param("estado") String estado,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);
}
