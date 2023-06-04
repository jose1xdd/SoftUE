package com.backend.softue.repositories;

import com.backend.softue.models.AreaConocimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaConocimientoRepository extends JpaRepository<AreaConocimiento, Integer> {
    AreaConocimiento findByNombre(String nombre);
}
