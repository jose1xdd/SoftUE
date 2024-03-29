package com.backend.softue.repositories;

import com.backend.softue.models.SingInToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface SingInTokenRepository extends JpaRepository<SingInToken,String> {
    @Query("SELECT t FROM SingInToken t WHERE t.fecha_caducidad <= :dateMinusOneDay")
    List<SingInToken> searchTokensByDate(@Param("dateMinusOneDay") LocalDateTime dateMinusOneDay);
    @Query("SELECT s FROM SingInToken s WHERE s.token = :token")
    SingInToken findByToken(@Param("token") String token);
    @Query("SELECT t FROM SingInToken t JOIN t.usuario_codigo u WHERE u.correo = :email")
    SingInToken findTokenByEmail(@Param("email") String email);


}
