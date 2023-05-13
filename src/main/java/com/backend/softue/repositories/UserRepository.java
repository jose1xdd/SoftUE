package com.backend.softue.repositories;

import com.backend.softue.models.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<Usuario,Integer>{
    
}
