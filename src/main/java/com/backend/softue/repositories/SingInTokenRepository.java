package com.backend.softue.repositories;

import com.backend.softue.models.SingInToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SingInTokenRepository extends JpaRepository<SingInToken,String> {
    @Query("SELECT t FROM SingInToken t WHERE t.fecha_caducidad <= :dateMinusOneDay")
    List<SingInToken> searchTokensByDate(@Param("dateMinusOneDay") LocalDateTime dateMinusOneDay);
    SingInToken findByToken(String token);

}
