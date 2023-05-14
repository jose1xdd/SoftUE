package com.backend.softue.security;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class Hashing {
    Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
    @Autowired
    JWTUtil jwt;
    public String hash (String password){

        return argon2.hash(1,1024,1,password);
    }
    public Boolean validate(String password,String passwordhashed){
        return argon2.verify(passwordhashed,password);
    }

    public String generarJWT(String id , String rol){
        return jwt.create(String.valueOf(id), rol);
    }
}
