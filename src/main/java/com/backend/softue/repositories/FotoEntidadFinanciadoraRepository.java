package com.backend.softue.repositories;

import com.backend.softue.models.FotoEntidadFinanciadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoEntidadFinanciadoraRepository extends JpaRepository<FotoEntidadFinanciadora, Integer>{
}
