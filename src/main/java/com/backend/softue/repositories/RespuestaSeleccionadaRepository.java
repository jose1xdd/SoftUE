package com.backend.softue.repositories;


import com.backend.softue.models.RespuestaSeleccionada;
import com.backend.softue.models.RespuestaSeleccionadaKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespuestaSeleccionadaRepository extends JpaRepository<RespuestaSeleccionada, RespuestaSeleccionadaKey> {

}
