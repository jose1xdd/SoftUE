package com.backend.softue.controllers;

import com.backend.softue.models.Docente;
import com.backend.softue.models.Estudiante;
import com.backend.softue.services.DocenteServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/docente")
public class DocenteController {

    @Autowired
    private DocenteServices docenteServices;
    @CheckSession(permitedRol ={"docente", "coordinador", "administrativo"})
    @PatchMapping("/update")
    public ResponseEntity<?> actualizar(@RequestHeader("X-Softue-JWT") String jwt, @Valid @RequestBody Docente docente, BindingResult bindingResult) {
        try {
           this.docenteServices.actualizarDocente(docente, jwt);
            return ResponseEntity.ok(new ResponseConfirmation("El docente ha sido actualizado correctamente"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
        }
    }

    @CheckSession(permitedRol ={"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping("/{email}")
    public ResponseEntity<?> visualizar(@RequestHeader("X-Softue-JWT") String jwt, @PathVariable String email) {
        try {
            return ResponseEntity.ok(this.docenteServices.obtenerDocente(email));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
        }
    }
}
