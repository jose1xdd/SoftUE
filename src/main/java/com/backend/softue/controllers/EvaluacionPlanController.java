package com.backend.softue.controllers;

import com.backend.softue.services.EvaluacionPlanServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/planNegocio/evaluacion")
public class EvaluacionPlanController {

    @Autowired
    private EvaluacionPlanServices evaluacionPlanServices;

    @CheckSession(permitedRol ={"docente"})
    @PostMapping("/{titulo}")
    public ResponseEntity<?> crear(@RequestHeader("X-Softue-JWT") String jwt, @PathVariable String titulo) {
        try {
            this.evaluacionPlanServices.crearEvaluacion(jwt, titulo);
            return ResponseEntity.ok(new ResponseConfirmation("La evaluacion del plan de negocio se creó correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol ={"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping("/{titulo}")
    public ResponseEntity<?> listar(@RequestHeader("X-Softue-JWT") String jwt, @PathVariable String titulo) {
        try {
            return ResponseEntity.ok(this.evaluacionPlanServices.listar(titulo, jwt));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }
}
