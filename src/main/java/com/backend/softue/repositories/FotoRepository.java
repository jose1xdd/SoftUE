package com.backend.softue.repositories;

import com.backend.softue.models.FotoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoRepository extends JpaRepository<FotoUsuario,Integer> {
    @Modifying
    @Query(value = "DELETE FROM Foto_usuario WHERE foto_usuario = :photoId", nativeQuery = true)
    void deleteFotoById(@Param("photoId") Integer photoId);



}
