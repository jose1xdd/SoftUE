package com.backend.softue.controllers;

import com.backend.softue.services.PlanNegocioServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/planNegocio")
public class PlanNegocioController {

    @Autowired
    private PlanNegocioServices planNegocioServices;

    @CheckSession(permitedRol = {"coordinador"})
    @PostMapping("/{titulo}")
    public ResponseEntity<?> crear(@RequestHeader("X-Softue-JWT") String jwt, @PathVariable String titulo) {
        try {
            this.planNegocioServices.crear(jwt, titulo);
            return ResponseEntity.ok(new ResponseConfirmation("Plan de negocio creado correctamente"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping("/{titulo}")
    public ResponseEntity<?> visualizar(@PathVariable String titulo) {
        try {
            return ResponseEntity.ok(this.planNegocioServices.obtenerPlanNegocio(titulo));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }
}
