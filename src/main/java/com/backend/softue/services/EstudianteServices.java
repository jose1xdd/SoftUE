package com.backend.softue.services;

import com.backend.softue.models.Estudiante;
import com.backend.softue.models.User;
import com.backend.softue.repositories.EstudianteRepository;
import com.backend.softue.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstudianteServices {
    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private UserServices usuarioServices;

    public void registrarEstudiante(Estudiante estudiante) {
        usuarioServices.registerUser((User) estudiante);
        estudianteRepository.save(estudiante);
    }

    public void actualizarEstudiante(Estudiante estudiante, String jwt) {
        usuarioServices.actualizarUsuario((User) estudiante, jwt);
        Estudiante result = this.estudianteRepository.findByCorreo(estudiante.getCorreo());
        estudiante.setCodigo(result.getCodigo());
        this.estudianteRepository.save(estudiante);
    }
}
