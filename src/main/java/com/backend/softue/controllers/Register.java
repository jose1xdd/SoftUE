package com.backend.softue.controllers;

import com.backend.softue.models.User;
import com.backend.softue.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user, BindingResult bindingResult) {

        return new ResponseEntity<>(this.userServices.registerUser(user), HttpStatus.OK);

    }
}
