package com.backend.softue.repositories;

import com.backend.softue.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByCorreo(String correo);
}
