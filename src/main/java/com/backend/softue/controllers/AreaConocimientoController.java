package com.backend.softue.controllers;

import com.backend.softue.services.AreaConocimientoServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/areaConocimiento")
public class AreaConocimientoController {

    @Autowired
    private AreaConocimientoServices areaConocimientoServices;

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping()
    public ResponseEntity<?> listar() {
        try {
            return ResponseEntity.ok(this.areaConocimientoServices.listar());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"coordinador", "administrativo"})
    @PostMapping()
    public ResponseEntity<?> agregarArea(@RequestParam String nombre) {
        try {
            this.areaConocimientoServices.agregar(nombre);
            return ResponseEntity.ok(new ResponseConfirmation("Area de conocimiento agregada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(), e.getMessage(), e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"coordinador", "administrativo"})
    @DeleteMapping()
    public ResponseEntity<?> eliminarArea(@RequestParam String nombre) {
        try {
            this.areaConocimientoServices.eliminar(nombre);
            return ResponseEntity.ok(new ResponseConfirmation("Area de conocimiento eliminada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(), e.getMessage(), e.getStackTrace()[0].toString()));
        }
    }
}
