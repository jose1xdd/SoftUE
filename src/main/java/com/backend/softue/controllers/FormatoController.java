package com.backend.softue.controllers;

import com.backend.softue.models.Formato;
import com.backend.softue.services.FormatoServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ErrorFactory;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import com.backend.softue.utils.response.ResponseToken;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/formato")
public class FormatoController {

    @Autowired
    private FormatoServices formatoServices;

    @Autowired
    private ErrorFactory errorFactory;

    @GetMapping(value = "/recuperar/{id}", produces = { "application/octet-stream", "application/pdf" })
    public ResponseEntity<?> recuperarFormato(@PathVariable String id) {
        try {
            String nombreArchivo = this.formatoServices.obtenerNombre(id);
            String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf("."));
            HttpHeaders headers = new HttpHeaders();
            if (extension.equals(".docx")) {
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }
            else if (extension.equals(".pdf")) {
                headers.setContentType(MediaType.APPLICATION_PDF);
            }
            else throw new RuntimeException("El formato del archivo no es apto para retornarse");
            headers.setContentDisposition(ContentDisposition.attachment().filename(nombreArchivo).build());
            return new ResponseEntity<>(this.formatoServices.obtenerFormato(id), headers, HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @PostMapping()
    public ResponseEntity<?> crearFormato(@Valid @RequestBody Formato formato, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessages = errorFactory.errorGenerator(bindingResult);
                return ResponseEntity.badRequest().body(new ResponseError("Input Error",errorMessages,"Bad Request"));
            }
            this.formatoServices.guardarFormato(formato);
            return new ResponseEntity<ResponseConfirmation>(new ResponseConfirmation("El formato se guardo correctamente"), HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }
}
