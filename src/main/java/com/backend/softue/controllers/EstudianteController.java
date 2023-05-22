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

    @CheckSession(permitedRol ={"estudiante", "coordinador", "administrativo"})
    @PatchMapping("/update")
    public ResponseEntity<?> actualizar(@RequestHeader("X-Softue-JWT") String jwt, @Valid @RequestBody Estudiante estudiante, BindingResult bindingResult) {
        try {
            this.estudianteServices.actualizarEstudiante(estudiante, jwt);
            return ResponseEntity.ok(new ResponseConfirmation("El estudiante ha sido actualizado correctamente"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
        }
    }

    @CheckSession(permitedRol ={"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping("/{email}")
    public ResponseEntity<?> visualizar(@RequestHeader("X-Softue-JWT") String jwt, @PathVariable String email) {
        System.out.println(email);
        try {
            return ResponseEntity.ok(this.estudianteServices.obtenerEstudiante(email));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
        }
    }
}
