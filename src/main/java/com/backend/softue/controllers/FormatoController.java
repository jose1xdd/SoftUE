package com.backend.softue.controllers;

import com.backend.softue.models.Formato;
import com.backend.softue.services.FormatoServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

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

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @PostMapping()
    public ResponseEntity<?> crearFormato(@RequestParam Integer id, @RequestParam String modulo, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate fechaCreacion, @RequestParam MultipartFile documento, @RequestParam String extension) {
        try {
            Formato formato = new Formato(id, modulo, fechaCreacion, documento.getBytes(), extension);
            this.formatoServices.guardarFormato(formato);
            return new ResponseEntity<ResponseConfirmation>(new ResponseConfirmation("El formato se guardo correctamente"), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<ResponseError>(new ResponseError(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarFormato(@PathVariable String id) {
        try {
            this.formatoServices.borrarFormato(id);
            return new ResponseEntity<ResponseConfirmation>(new ResponseConfirmation("El formato se borro correctamente"), HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping("/listar")
    public ResponseEntity<?> listarFormatos() {
        try {
            return new ResponseEntity<List<Formato>>(this.formatoServices.obtenerListado(), HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
        }
    }
}
