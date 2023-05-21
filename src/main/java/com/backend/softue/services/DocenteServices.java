package com.backend.softue.services;

import com.backend.softue.models.Docente;
import com.backend.softue.models.Estudiante;
import com.backend.softue.models.User;
import com.backend.softue.repositories.DocenteRepository;
import com.backend.softue.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocenteServices {
    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private UserServices usuarioServices;
    public void registrarDocente(Docente docente) {
        usuarioServices.registerUser((User) docente);
        docenteRepository.save(docente);
    }
}