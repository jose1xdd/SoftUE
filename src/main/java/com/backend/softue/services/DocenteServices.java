package com.backend.softue.services;

import com.backend.softue.models.*;
import com.backend.softue.repositories.DocenteRepository;
import com.backend.softue.repositories.EstudianteRepository;
import com.backend.softue.repositories.SingInTokenRepository;
import com.backend.softue.repositories.UsuarioDeshabilitadoRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DocenteServices {
    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private UsuarioDeshabilitadoRepository usuarioDeshabilitadoRepository;

    @Autowired
    private UserServices usuarioServices;

    @Autowired
    private SingInTokenRepository singInTokenRepository;

    public void registrarDocente(Docente docente) {
        if(!docente.getTipoUsuario().equals("docente")) throw new RuntimeException("No se puede registrar este usuario, no es un docente");
        usuarioServices.registerUser((User) docente);
        docenteRepository.save(docente);
    }

    public void actualizarDocente(Docente docente, String jwt) {
        if(!docente.getTipoUsuario().equals("docente")) throw new RuntimeException("No se puede actualizar este usuario, no se puede cambiar de rol");
        usuarioServices.actualizarUsuario((User) docente, jwt);
        Docente result = this.docenteRepository.findByCorreo(docente.getCorreo());
        docente.setCodigo(result.getCodigo());
        docente.setContrasenia(result.getContrasenia());
        this.docenteRepository.save(docente);
    }
    public Docente obtenerDocente(String email) {
        if (email != null) {
            Docente result = this.docenteRepository.findByCorreo(email);
            if (result == null) throw new RuntimeException("El docente no existe");
            if(result.getFoto_usuario() != null) result.setFotoUsuarioId(result.getFoto_usuario().getId());
            return result;
        }
        throw new RuntimeException("No se envió información con la que buscar al usuario");
    }


    public void deshabilitarDocente(String email) {
        if (email != null) {
            Docente result = this.docenteRepository.findByCorreo(email);
            if(result == null) throw new RuntimeException("El usuario no existe");
            this.usuarioDeshabilitadoRepository.save(new UsuarioDeshabilitado(result));
            SingInToken singInToken = this.singInTokenRepository.findTokenByEmail(email);
            if(singInToken != null) this.singInTokenRepository.delete(singInToken);
            this.docenteRepository.delete(result);
        }
        else throw new RuntimeException("No se envió información con la que buscar al usuario");
    }

    public  List<Docente> listarDocentes() throws IOException {
        List<Docente> docentes = this.docenteRepository.findAll();
        if(docentes.isEmpty()) throw new RuntimeException("No hay Docentes registrados");
        return docentes;
    }
}
