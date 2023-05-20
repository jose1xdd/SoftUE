package com.backend.softue.controllers;

import com.backend.softue.services.EstudianteServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EstudianteController {

    @Autowired
    private EstudianteServices estudianteServices;

}
