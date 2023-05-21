package com.backend.softue.repositories;

import com.backend.softue.models.EntidadFinanciadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntidadFinanciadoraRepository extends JpaRepository<EntidadFinanciadora, Integer> {
    EntidadFinanciadora findByCorreo(String correo);
}
