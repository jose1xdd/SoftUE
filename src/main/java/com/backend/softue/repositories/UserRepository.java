package com.backend.softue.repositories;

import com.backend.softue.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByCorreo(String correo);

    @Query("SELECT u FROM User u WHERE u.tipoUsuario = :tipoU")
    List<User> findByTipoUsuario(@Param("tipoU") String tipoUsuario);


}
