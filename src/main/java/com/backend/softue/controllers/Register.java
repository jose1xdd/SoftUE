package com.backend.softue.controllers;

import com.backend.softue.models.User;
import com.backend.softue.services.UserServices;
import com.backend.softue.utils.response.ErrorFactory;
import com.backend.softue.utils.response.ResponseError;
import com.backend.softue.utils.response.ResponseToken;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@RestController
@RequestMapping("/Register")
public class Register {
    @Autowired
    UserServices userServices;
    @Autowired
    ErrorFactory errorFactory;
    @PostMapping()
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult bindingResult) {
       try {
           if (bindingResult.hasErrors()) {
               String errorMessages = errorFactory.errorGenerator(bindingResult);
               return new ResponseEntity<ResponseError>(new ResponseError(errorMessages), HttpStatus.BAD_REQUEST);
           }
           return new ResponseEntity<ResponseToken>(new ResponseToken(this.userServices.registerUser(user)), HttpStatus.OK);
       }
       catch (Exception e ){
            return new ResponseEntity<ResponseError>(new ResponseError(e.getMessage()), HttpStatus.BAD_REQUEST);
       }
    }
}
