package com.backend.softue.controllers;

import com.backend.softue.models.User;
import com.backend.softue.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/Register")
public class Register {
    @Autowired
    UserServices userServices;

    @PostMapping()
    public String registerUser(@RequestBody User user) {

        return this.userServices.registerUser(user);

    }
}
