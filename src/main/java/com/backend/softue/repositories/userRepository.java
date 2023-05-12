package com.backend.softue.repositories;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface userRepository extends CrudRepository<User,Integer>{
    
}
