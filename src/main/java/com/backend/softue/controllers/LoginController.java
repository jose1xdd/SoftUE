package com.backend.softue.controllers;

import com.backend.softue.security.JWTUtil;
import com.backend.softue.services.UserServices;
import com.backend.softue.utils.auxiliarClases.LoginResponse;
import com.backend.softue.utils.response.ErrorFactory;
import com.backend.softue.utils.response.ResponseError;
import com.backend.softue.utils.response.ResponseToken;
import jakarta.validation.Valid;
import org.apache.tomcat.Jar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    UserServices userServices;
    @Autowired
    ErrorFactory errorFactory;

    @Autowired
    JWTUtil jwtUtil;
    @PostMapping()
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginResponse user, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessages = errorFactory.errorGenerator(bindingResult);
                return new ResponseEntity<ResponseError>(new ResponseError(errorMessages), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<ResponseToken>(new ResponseToken(this.userServices.login(user)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseError>(new ResponseError(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


}

