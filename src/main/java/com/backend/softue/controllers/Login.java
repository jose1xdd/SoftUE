package com.backend.softue.controllers;

import com.backend.softue.security.JWTUtil;
import com.backend.softue.services.UserServices;
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
    JWTUtil jwt;

    @GetMapping(path = "/{token}")
    public Boolean login(@PathVariable("token")String token) {
       return jwt.isTokenExpired(token);


    }

}
