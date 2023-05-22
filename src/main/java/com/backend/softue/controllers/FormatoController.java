package com.backend.softue.controllers;

import com.backend.softue.services.FormatoServices;
import com.backend.softue.utils.response.ResponseError;
import com.backend.softue.utils.response.ResponseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/formato")
public class FormatoController {

    @Autowired
    private FormatoServices formatoServices;

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
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
        }
    }
}
