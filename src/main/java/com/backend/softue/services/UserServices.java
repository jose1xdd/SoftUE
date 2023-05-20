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




    @Autowired
    public String login() {
        return "asdas";
    }

    public String registerUser(User user) {
        User result = this.userRepository.findByCorreo(user.getCorreo());
        if (result != null) return "User al ready exist";
        user.setContrasenia(encrypt.hash(user.getContrasenia()));
        User userData = this.userRepository.save(user);
        String jwt = this.encrypt.generarJWT(user.getCorreo(), user.getTipo_usuario());
        LocalDateTime newDateTime = LocalDateTime.now().plus(Duration.ofHours(1));
        this.singInTokenRepository.save(new SingInToken(jwt, newDateTime, userData));
        return jwt;
    }
}
