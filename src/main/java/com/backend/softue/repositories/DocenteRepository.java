package com.backend.softue.repositories;

import com.backend.softue.models.Docente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocenteRepository extends JpaRepository<Docente, Integer> {
    Docente findByCorreo(String correo);
    List<Docente> findByArea(String area);

}
