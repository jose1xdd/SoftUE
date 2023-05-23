package com.backend.softue.controllers;

import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.services.IdeaNegocioServices;
import com.backend.softue.services.IdeaPlanteadaServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ErrorFactory;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/ideaNegocio")
public class IdeaNegocioController {

    @Autowired
    private IdeaNegocioServices ideaNegocioServices;

    @Autowired
    private IdeaPlanteadaServices ideaPlanteadaServices;

    @Autowired
    private ErrorFactory errorFactory;

    @CheckSession(permitedRol ={"estudiante"})
    @PostMapping()
    public ResponseEntity<?> crear(@RequestHeader("X-Softue-JWT") String jwt, @RequestParam String titulo, @RequestParam String[] integrantes, @RequestParam String area, @RequestParam MultipartFile documento) {
        try {

            IdeaNegocio ideaNegocio = new IdeaNegocio(null, titulo, 'F', area, null, LocalDate.now(), null, null, null, null, null, null, null);
            this.ideaNegocioServices.crear(ideaNegocio, integrantes, documento.getBytes(), jwt);
            return ResponseEntity.ok(new ResponseConfirmation("Idea de negocio creada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

}
