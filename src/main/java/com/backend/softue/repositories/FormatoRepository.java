package com.backend.softue.repositories;

import com.backend.softue.models.Formato;
import com.backend.softue.models.PlanNegocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface FormatoRepository extends JpaRepository<Formato, Integer> {

    @Query(value = "SELECT id, modulo, fecha_creacion FROM formato", nativeQuery = true)
    List<Formato> obtenerListado();

    @Query(value = "SELECT * FROM formato " +
            "WHERE (modulo = 'idea_de_negocio' )" +
            "ORDER BY id DESC " +
            "LIMIT 1" ,
            nativeQuery = true)
    Formato FormatoRecienteIdea();

    @Query(value = "SELECT * FROM formato " +
            "WHERE (modulo = 'plan_de_negocio' )" +
            "ORDER BY id DESC " +
            "LIMIT 1" ,
            nativeQuery = true)
    Formato FormatoRecientePlan();

    @Query(value = "SELECT * FROM formato " +
            "WHERE (modulo = 'material_idea_negocio' )" +
            "ORDER BY id DESC " ,nativeQuery = true)
    List<Formato> obtenerMaterialIdea();

    @Query(value = "SELECT * FROM formato " +
            "WHERE (modulo = 'material_plan_negocio' )" +
            "ORDER BY id DESC " ,nativeQuery = true)
    List<Formato> obtenerMaterialPlan();

    @Query(value = "SELECT * FROM formato " +
            "WHERE (modulo = 'material_general' )" +
            "ORDER BY id DESC " ,nativeQuery = true)
    List<Formato> obtenerMaterialGeneral();

    @Query(value = "SELECT * FROM formato WHERE modulo = 'idea_de_negocio' OR modulo = 'plan_de_negocio'", nativeQuery = true)
    List<Formato> obtenerFormatoPlantillla();

    @Query(value = "SELECT * FROM formato WHERE modulo = 'material_idea_negocio' OR modulo = 'material_plan_negocio' OR modulo = 'material_general'", nativeQuery = true)
    List<Formato> obtenerMaterial();
}
