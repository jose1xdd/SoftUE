package com.backend.softue.controllers;

import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.services.CalificacionIdeaServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/ideaNegocio/calificacion")
public class CalificacionIdeaController {

    @Autowired
    private CalificacionIdeaServices calificacionIdeaServices;

    @CheckSession(permitedRol ={"coordinador"})
    @PostMapping("/{titulo}/{correo}")
    public ResponseEntity<?> crear(@PathVariable String titulo, @PathVariable String correo) {
        try {
            this.calificacionIdeaServices.crear(titulo, correo);
            return ResponseEntity.ok(new ResponseConfirmation("Se ha asignado un docente a una evaluación de idea de negocio con éxito."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }
}
