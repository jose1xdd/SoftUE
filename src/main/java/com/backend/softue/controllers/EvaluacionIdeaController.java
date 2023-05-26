package com.backend.softue.controllers;

import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.services.EvaluacionIdeaServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/ideaNegocio/evaluacion")
public class EvaluacionIdeaController {

    @Autowired
    private EvaluacionIdeaServices evaluacionIdeaServices;

    @CheckSession(permitedRol ={"docente"})
    @PostMapping("/{titulo}")
    public ResponseEntity<?> crear(@RequestHeader("X-Softue-JWT") String jwt, @PathVariable String titulo) {
        try {
            this.evaluacionIdeaServices.crearEvaluacion(jwt, titulo);
            return ResponseEntity.ok(new ResponseConfirmation("La evaluacion de la idea de negocio se creó correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol ={"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping("/{titulo}")
    public ResponseEntity<?> listar(@PathVariable String titulo) {
        try {
            return ResponseEntity.ok(this.evaluacionIdeaServices.listar(titulo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }
}
