package com.backend.softue.controllers;

import com.backend.softue.services.PreguntaServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ErrorFactory;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pregunta")
public class PreguntaController {

    @Autowired
    private PreguntaServices preguntaServices;

    @Autowired
    private ErrorFactory errorFactory;

    @CheckSession(permitedRol = {"coordinador"})
    @PostMapping()
    public ResponseEntity<?> crear(@RequestParam String enunciado, @RequestParam String nombreComponente) {
        try {
            this.preguntaServices.crear(enunciado, nombreComponente);
            return ResponseEntity.ok(new ResponseConfirmation("Pregunta registrada correctamente"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"coordinador"})
    @GetMapping()
    public ResponseEntity<?> listar() {
        try {
            return ResponseEntity.ok(this.preguntaServices.listar());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"coordinador"})
    @PatchMapping()
    public ResponseEntity<?> actualizar(@RequestParam String id, @RequestParam String enunciado, @RequestParam String nombreComponente) {
        try {
            this.preguntaServices.actualizar(id, enunciado, nombreComponente);
            return ResponseEntity.ok(new ResponseConfirmation("Pregunta actualizada correctamente"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"coordinador"})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            this.preguntaServices.eliminar(id);
            return ResponseEntity.ok(new ResponseConfirmation("Pregunta eliminada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }
}
