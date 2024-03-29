package com.backend.softue.controllers;

import com.backend.softue.models.Docente;
import com.backend.softue.models.Estudiante;
import com.backend.softue.models.User;
import com.backend.softue.services.DocenteServices;
import com.backend.softue.services.EstudianteServices;
import com.backend.softue.services.UserServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ErrorFactory;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private UserServices userServices;

    @Autowired
    private EstudianteServices estudianteServices;

    @Autowired
    private DocenteServices docenteServices;

    @Autowired
    private ErrorFactory errorFactory;


    @PostMapping()
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessages = errorFactory.errorGenerator(bindingResult);
                return ResponseEntity.badRequest().body(new ResponseError("Input Error",errorMessages,"Bad Request"));
            }
            this.userServices.registerUser(user);
            return new ResponseEntity<ResponseConfirmation>(new ResponseConfirmation("Usuario Registrado Correctamente"), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @PostMapping("/estudiante")
    public ResponseEntity<?> registrarEstudiante(@Valid @RequestBody Estudiante estudiante, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessages = errorFactory.errorGenerator(bindingResult);
                return ResponseEntity.badRequest().body(new ResponseError("Input Error",errorMessages,"Bad Request"));
            }
            this.estudianteServices.registrarEstudiante(estudiante);
            return new ResponseEntity<ResponseConfirmation>(new ResponseConfirmation("El estudiante se registro correctamente"), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @PostMapping("/estudiante/codigo")
    public ResponseEntity<?> registrarEstudiante(@RequestParam String codigo, @RequestParam String contrasenia) {
        try {
            this.estudianteServices.registrarEstudiante(codigo, contrasenia);
            return new ResponseEntity<ResponseConfirmation>(new ResponseConfirmation("El estudiante se registro correctamente"), HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol ={"administrativo"})
    @PostMapping("/estudiante/archivo")
    public ResponseEntity<?> registrarEstudiantesDesdeArhivo(@RequestParam MultipartFile file) {
        try {
            return ResponseEntity.ok(this.estudianteServices.cargarEstudiantes(file));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol ={"administrativo"})
    @PostMapping("/docente/archivo")
    public ResponseEntity<?> registrarDocentesDesdeArhivo(@RequestParam MultipartFile file) {
        try {
            this.docenteServices.cargarDocentes(file);
            return ResponseEntity.ok(new ResponseConfirmation("Docentes cargados en el sistema exitosamente"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @PostMapping("/docente")
    public ResponseEntity<?> registrarDocente(@Valid @RequestBody Docente docente, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessages = errorFactory.errorGenerator(bindingResult);
                return ResponseEntity.badRequest().body(new ResponseError("Input Error",errorMessages,"Bad Request"));
            }
            this.docenteServices.registrarDocente(docente);
            return new ResponseEntity<ResponseConfirmation>(new ResponseConfirmation("El docente se registro correctamente"), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));

        }
    }

    @PostMapping("/docente/correo")
    public ResponseEntity<?> registrarDocente(@RequestParam String correo, @RequestParam String contrasenia) {
        try {
            this.docenteServices.registrarDocente(correo, contrasenia);
            return new ResponseEntity<ResponseConfirmation>(new ResponseConfirmation("El docente se registro correctamente"), HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }
}
