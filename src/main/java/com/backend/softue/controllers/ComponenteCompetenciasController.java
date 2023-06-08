package com.backend.softue.controllers;

import com.backend.softue.models.ComponenteCompetencias;
import com.backend.softue.services.ComponenteCompetenciasServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ErrorFactory;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/componenteCompetencias")
public class ComponenteCompetenciasController {

    @Autowired
    private ComponenteCompetenciasServices componenteCompetenciasServices;

    @Autowired
    private ErrorFactory errorFactory;

    @CheckSession(permitedRol = {"coordinador"})
    @PostMapping()
    public ResponseEntity<?> crear(@Valid @RequestBody ComponenteCompetencias componenteCompetencias, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessages = errorFactory.errorGenerator(bindingResult);
                return ResponseEntity.badRequest().body(new ResponseError("Input Error", errorMessages, "Bad Request"));
            }
            this.componenteCompetenciasServices.crear(componenteCompetencias);
            return ResponseEntity.ok(new ResponseConfirmation("Componente de competencias registrado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"coordinador"})
    @GetMapping()
    public ResponseEntity<?> listar() {
        try {
            return ResponseEntity.ok(this.componenteCompetenciasServices.listar());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"coordinador"})
    @DeleteMapping("/{nombre}")
    public ResponseEntity<?> eliminar(@PathVariable String nombre) {
        try {
            this.componenteCompetenciasServices.eliminar(nombre);
            return ResponseEntity.ok(new ResponseConfirmation("Componente de competencias eliminada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"coordinador"})
    @PatchMapping()
    public ResponseEntity<?> actualizar(@Valid @RequestBody ComponenteCompetencias componenteCompetencias, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessages = errorFactory.errorGenerator(bindingResult);
                return ResponseEntity.badRequest().body(new ResponseError("Input Error", errorMessages, "Bad Request"));
            }
            this.componenteCompetenciasServices.actualizar(componenteCompetencias);
            return ResponseEntity.ok(new ResponseConfirmation("Componente de compentencias actualizada correctamente"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }
}
