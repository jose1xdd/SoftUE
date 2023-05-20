package com.backend.softue.services;

import com.backend.softue.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstudianteServices {
    @Autowired
    private EstudianteRepository estudianteRepository;

}
