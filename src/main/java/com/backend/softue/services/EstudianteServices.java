package com.backend.softue.services;

import com.backend.softue.models.*;
import com.backend.softue.repositories.EstudianteRepository;
import com.backend.softue.repositories.SingInTokenRepository;
import com.backend.softue.repositories.UserRepository;
import com.backend.softue.repositories.UsuarioDeshabilitadoRepository;
import com.backend.softue.utils.GradosPermitidos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EstudianteServices {
    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private UsuarioDeshabilitadoRepository usuarioDeshabilitadoRepository;

    @Autowired
    private UserServices usuarioServices;

    @Autowired
    private SingInTokenRepository singInTokenRepository;

    @Autowired
    private GradosPermitidos gradosPermitidos;

    public void registrarEstudiante(Estudiante estudiante) {

        if (!gradosPermitidos.getGrados().contains(estudiante.getCurso()))
            throw new RuntimeException("No se puede registrar este usuario, ya que el curso diligenciado no es valido");
        if (!estudiante.getTipoUsuario().equals("estudiante"))
            throw new RuntimeException("No se puede registrar este usuario, no es un estudiante");
        usuarioServices.registerUser((User) estudiante);
        estudianteRepository.save(estudiante);
    }

    public void actualizarEstudiante(Estudiante estudiante, String jwt) {
        if (!gradosPermitidos.getGrados().contains(estudiante.getCurso()))
            throw new RuntimeException("No se puede registrar este usuario, ya que el curso diligenciado no es valido");
        if (!estudiante.getTipoUsuario().equals("estudiante"))
            throw new RuntimeException("No se puede actualizar este usuario, no se puede cambiar de rol");
        usuarioServices.actualizarUsuario((User) estudiante, jwt);
        Estudiante result = this.estudianteRepository.findByCorreo(estudiante.getCorreo());
        estudiante.setCodigo(result.getCodigo());
        estudiante.setContrasenia(result.getContrasenia());
        this.estudianteRepository.save(estudiante);
    }

    public Estudiante obtenerEstudiante(String email) {
        if (email != null) {
            Estudiante result = this.estudianteRepository.findByCorreo(email);
            if (result == null) throw new RuntimeException("El estudiante no existe");
            if (result.getFoto_usuario() != null) result.setFotoUsuarioId(result.getFoto_usuario().getId());
            return result;
        }
        throw new RuntimeException("No se envió información con la que buscar al usuario");
    }

    public void deshabilitarEstudiante(String email) {
        if (email != null) {
            Estudiante result = this.estudianteRepository.findByCorreo(email);
            if (result == null) throw new RuntimeException("El usuario no existe");
            this.usuarioDeshabilitadoRepository.save(new UsuarioDeshabilitado(result));
            SingInToken singInToken = this.singInTokenRepository.findTokenByEmail(email);
            if (singInToken != null) this.singInTokenRepository.delete(singInToken);
            this.estudianteRepository.delete(result);
        } else throw new RuntimeException("No se envió información con la que buscar al usuario");
    }

    public List<Estudiante> listarEstudiantes() {
        return this.estudianteRepository.findAll();
    }

    public List<Estudiante> listarEstudiantesCurso(String curso) {
        if (!gradosPermitidos.getGrados().contains(curso))
            throw new RuntimeException("No se puede registrar este usuario, ya que el curso diligenciado no es valido");
        return this.estudianteRepository.findByCurso(curso);

    }
}
