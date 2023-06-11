package com.backend.softue.controllers;

import com.backend.softue.services.TestServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
            return ResponseEntity.ok(new ResponseConfirmation("Test registrao correctamente"));
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
}
