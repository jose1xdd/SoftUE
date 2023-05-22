package com.backend.softue.repositories;

import com.backend.softue.models.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken,String> {
    @Query("SELECT t FROM ResetToken t WHERE t.fecha_caducidad <= :dateMinusOneDay")
    List<ResetToken> searchTokensByDate(@Param("dateMinusOneDay") LocalDateTime dateMinusOneDay);
    @Query("SELECT s FROM ResetToken s WHERE s.token = :token")
    ResetToken findByToken(@Param("token") String token);
    @Query("SELECT t FROM ResetToken t JOIN t.usuario_codigo u WHERE u.correo = :email")
    ResetToken findTokenByEmail(@Param("email") String email);

}
