package com.backend.softue.services;

import com.backend.softue.models.SingInToken;
import com.backend.softue.models.User;
import com.backend.softue.repositories.SingInTokenRepository;
import com.backend.softue.repositories.UserRepository;
import com.backend.softue.security.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class UserServices {
    @Autowired
    SingInTokenRepository singInTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    Hashing encrypt;


    public SingInToken login(String token) {
        SingInToken x = this.singInTokenRepository.findByToken(token);
        x.setUserCod(x.getUsuario_codigo().getCodigo());
        return x;
    }

    public String registerUser(User user) {
        User result = this.userRepository.findByCorreo(user.getCorreo());
        if (result != null) throw new RuntimeException("User already exists");
        user.setContrasenia(encrypt.hash(user.getContrasenia()));
        User userData = this.userRepository.save(user);
        String jwt = this.encrypt.generarJWT("" + userData.getCorreo(), user.getCorreo());
        LocalDateTime newDateTime = LocalDateTime.now().plus(Duration.ofHours(1));
        this.singInTokenRepository.save(new SingInToken(jwt, newDateTime, userData, userData.getCodigo()));

        return jwt;
    }
}
