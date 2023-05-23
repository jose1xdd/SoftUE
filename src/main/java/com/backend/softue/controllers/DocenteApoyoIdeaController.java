package com.backend.softue.controllers;

import com.backend.softue.services.DocenteApoyoIdeaServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/docenteApoyo")
public class DocenteApoyoIdeaController {

    @Autowired
    private DocenteApoyoIdeaServices docenteApoyoIdeaServices;

    @CheckSession(permitedRol = {"docente"})
    @GetMapping()
    public ResponseEntity<?> agregarDocenteApoyo(@RequestHeader("X-Softue-JWT") String jwt, @RequestParam String tituloIdea, @RequestParam String correoDocente) {
        try {
            this.docenteApoyoIdeaServices.agregarDocenteApoyo(jwt, tituloIdea, correoDocente);
            return ResponseEntity.ok(new ResponseConfirmation("El docente de apoyo se agrego correctamete a la idea de negocio"));
        }
        catch (Exception e) {

            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }
}
