package com.backend.softue.controllers;


import com.backend.softue.services.RespuestaServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/respuesta")
public class RespuestaController {

    @Autowired
    private RespuestaServices respuestaServices;

    @CheckSession(permitedRol = {"coordinador"})
    @PostMapping()
    public ResponseEntity<?> crear(@RequestParam String contenido, @RequestParam Integer valor, @RequestParam Integer preguntaId) {
        try {
            this.respuestaServices.crear(contenido, valor, preguntaId);
            return ResponseEntity.ok(new ResponseConfirmation("Respuesta registrada correctamente"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"coordinador"})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            this.respuestaServices.eliminar(id);
            return ResponseEntity.ok(new ResponseConfirmation("Respuesta eliminada correctamente"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"coordinador"})
    @PatchMapping()
    public ResponseEntity<?> actualizar(@RequestParam Integer id, @RequestParam String contenido, @RequestParam Integer valor, @RequestParam Integer preguntaId) {
        try {
            this.respuestaServices.actualizar(id, contenido, valor, preguntaId);
            return ResponseEntity.ok(new ResponseConfirmation("Respuesta actualizada correctamente"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"coordinador", "estudiante"})
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerRespuestas(@PathVariable String id) {
        try {
            return ResponseEntity.ok(this.respuestaServices.obtenerRespuestas(Integer.parseInt(id)));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }
}
