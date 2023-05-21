package com.backend.softue.controllers;

import com.backend.softue.models.Estudiante;
import com.backend.softue.services.EstudianteServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/estudiante")
public class EstudianteController {

    @Autowired
    private EstudianteServices estudianteServices;

    @GetMapping("/hola")
    public String hello() {
        return "HOLA MUNDO";
    }

    @CheckSession(permitedRol ={"estudiante", "coordinador", "administrativo"})
    @PatchMapping("/update")
    public ResponseEntity<?> actualizar(@RequestHeader("X-Softue-JWT") String jwt, @Valid @RequestBody Estudiante estudiante, BindingResult bindingResult) {
        try {
            this.estudianteServices.actualizarEstudiante(estudiante, jwt);
            return ResponseEntity.ok(new ResponseConfirmation("El usuario ha sido actualizado correctamente"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
        }
    }
}
