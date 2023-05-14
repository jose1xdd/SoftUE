package com.backend.softue.services;

import com.backend.softue.models.User;
import com.backend.softue.repositories.UserRepository;
import com.backend.softue.security.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServices {
    @Autowired
    UserRepository userRepository;
    @Autowired
    Hashing encrypt;
    @Autowired
    public String login(){
        return "asdas";
    }
    public String registerUser(User user){
            User result = this.userRepository.findByCorreo(user.getCorreo());
            if (result != null) return "User al ready exist";
            user.setContrasenia(encrypt.hash(user.getContrasenia()));
            String jwt = encrypt.generarJWT(user.getCorreo(),user.getTipo_usuario());
            this.userRepository.save(user);
            return jwt;
    }
}
