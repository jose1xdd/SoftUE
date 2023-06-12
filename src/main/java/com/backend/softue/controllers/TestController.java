package com.backend.softue.controllers;

import com.backend.softue.models.Test;
import com.backend.softue.services.TestServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestServices testServices;

    @CheckSession(permitedRol = {"estudiante"})
    @PostMapping()
    public ResponseEntity<?> crear(@RequestParam Integer codigoEstudiante, @RequestParam Integer respuestasId[]) {
        try {
            this.testServices.crear(codigoEstudiante, respuestasId, LocalDate.now());
            return ResponseEntity.ok("La respuesta se creo correctamente");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping()
    public  ResponseEntity<?> listar() {
        try {
            return ResponseEntity.ok(this.testServices.listar());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping(value = "/resultados/{id}")
    public ResponseEntity<?> obtenerResultadosByTest(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(this.testServices.obtenerResultadosByTest(id));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping(value = "/resultadosEstudiante/{codigo}")
    public ResponseEntity<?> obtenerResultadoByEstudiante(@PathVariable Integer codigo) {
        try {
            return ResponseEntity.ok((this.testServices.obtenerResultadosByEstudiante(codigo)));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"coordinador", "administrativo", "estudiante"})
    @PostMapping("/filtrar")
    public ResponseEntity<?> filtrarResultado(
            @RequestParam(required = false) Integer codigo,
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            List<Test> tests = this.testServices.filtrar(codigo, curso, fechaInicio, fechaFin);
            return ResponseEntity.ok(tests);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }
}
