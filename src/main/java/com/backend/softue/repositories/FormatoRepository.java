package com.backend.softue.repositories;

import com.backend.softue.models.Formato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FormatoRepository extends JpaRepository<Formato, Integer> {

    @Query(value = "SELECT id, modulo, fecha_creacion FROM formato", nativeQuery = true)
    List<Formato> obtenerListado();
}
