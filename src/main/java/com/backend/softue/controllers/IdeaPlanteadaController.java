package com.backend.softue.controllers;

import com.backend.softue.services.IdeaPlanteadaServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/ideaPlanteada")
public class IdeaPlanteadaController {

    @Autowired
    private IdeaPlanteadaServices ideaPlanteadaServices;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("pingpong");
    }
    @CheckSession(permitedRol ={"docente"})
    @DeleteMapping("/{titulo}/{correo}")
    public ResponseEntity<?> eliminar(@RequestHeader("X-Softue-JWT") String jwt, @PathVariable String titulo, @PathVariable String correo) {
        try {
            this.ideaPlanteadaServices.eliminarIntegrante(jwt, titulo, correo);
            return ResponseEntity.ok(new ResponseConfirmation("Integrante eliminado de la idea de negocio correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol ={"docente"})
    @PostMapping("/{titulo}/{correo}")
    public ResponseEntity<?> agregar(@RequestHeader("X-Softue-JWT") String jwt, @PathVariable String titulo, @PathVariable String correo) {
        try {
            this.ideaPlanteadaServices.agregarIntegrante(jwt, titulo, correo);
            return ResponseEntity.ok(new ResponseConfirmation("Integrante agregado a la idea de negocio correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }
}
