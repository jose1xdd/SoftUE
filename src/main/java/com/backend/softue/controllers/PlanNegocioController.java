package com.backend.softue.controllers;

import com.backend.softue.services.PlanNegocioServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @CheckSession(permitedRol = {"estudiante"})
    @PatchMapping()
    public ResponseEntity<?> actualizar(@RequestHeader("X-Softue-JWT") String jwt, @RequestParam String titulo, @RequestParam String resumen) {
        try {
            this.planNegocioServices.actualizarResumen(titulo, resumen, jwt);
            return ResponseEntity.ok(new ResponseConfirmation("El resumen del plan de negocio ha sido actualizado correctamente"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol ={"estudiante"})
    @PostMapping("/agregarDocumento")
    public ResponseEntity<?> agregarFormato(@RequestParam String titulo, @RequestParam MultipartFile documento) {
        try {
            //this.planNegocioServices.agregarDocumento(titulo, documento.getBytes(), documento.getOriginalFilename());
            return ResponseEntity.ok(new ResponseConfirmation("El formato del plan de negocio se agrego correctamete"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }
}
