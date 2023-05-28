package com.backend.softue.controllers;


import com.backend.softue.models.CalificacionPlanKey;
import com.backend.softue.services.CalificacionPlanServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ErrorFactory;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/planNegocio/calificacion")
public class CalificacionPlanController {

    @Autowired
    private CalificacionPlanServices calificacionPlanServices;

    @Autowired
    private ErrorFactory errorFactory;

    @CheckSession(permitedRol ={"coordinador"})
    @PostMapping("/{titulo}/{correo}")
    public ResponseEntity<?> crear(@PathVariable String titulo, @PathVariable String correo) {
        try {
            this.calificacionPlanServices.crear(titulo, correo, null);
            return ResponseEntity.ok(new ResponseConfirmation("Se ha asignado un docente a una evaluación de un plan de negocio con éxito."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol ={"coordinador"})
    @PostMapping()
    public ResponseEntity<?> crearConFechaCorte(@RequestParam String titulo, @RequestParam String correo, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaCorte) {
        try {
            this.calificacionPlanServices.crear(titulo, correo, fechaCorte);
            return ResponseEntity.ok(new ResponseConfirmation("Se ha asignado un docente a una evaluación de un plan de negocio con éxito y se ha actualizado la fecha corte del plan de negocio."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol ={"coordinador"})
    @DeleteMapping()
    public ResponseEntity<?> eliminar(@Valid @RequestBody CalificacionPlanKey id, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessages = errorFactory.errorGenerator(bindingResult);
                return ResponseEntity.badRequest().body(new ResponseError("Input Error",errorMessages,"Bad Request"));
            }
            this.calificacionPlanServices.eliminar(id);
            return ResponseEntity.ok(new ResponseConfirmation("Se ha eliminado un docente de una evaluación de idea de negocio con éxito."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol ={"docente"})
    @PatchMapping()
    public ResponseEntity<?> actualizar(@RequestHeader("X-Softue-JWT") String jwt, @RequestParam String titulo, @RequestParam String nota, @RequestParam String observacion) {
        try {
            this.calificacionPlanServices.actualizar(titulo, nota, observacion, jwt);
            return ResponseEntity.ok(new ResponseConfirmation("Se ha actualizado con éxito la calificación del docente."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }
}
