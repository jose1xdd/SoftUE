package com.backend.softue.controllers;

import com.backend.softue.models.SingInToken;
import com.backend.softue.models.User;
import com.backend.softue.security.JWTUtil;
import com.backend.softue.services.UserServices;
import com.backend.softue.utils.checkSession.CheckSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class Login {
    @Autowired
    UserServices userServices;
    @Autowired
    JWTUtil jwtUtil;
    @CheckSession(permitedRol = {"user"})
    @GetMapping("/{token}")
    public String login(@PathVariable("token") String token) {
        return this.jwtUtil.getKey(token);

    }


}
