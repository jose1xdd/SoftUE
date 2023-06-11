package com.backend.softue.controllers;

import com.backend.softue.models.Formato;
import com.backend.softue.services.FormatoServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import com.backend.softue.utils.response.ErrorFactory;
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

    @Autowired
    private ErrorFactory errorFactory;

    @GetMapping(value = "/recuperar/{id}", produces = { "application/octet-stream", "application/pdf" })
    public ResponseEntity<?> recuperarFormato(@PathVariable String id) {
        try {
            String nombreArchivo = this.formatoServices.obtenerNombre(id);
            Formato formato = this.formatoServices.obtenerFormato(id);
            HttpHeaders headers = new HttpHeaders();
            if (formato.getExtension().equals(".docx")) {
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }
            else if (formato.getExtension().equals(".pdf")) {
                headers.setContentType(MediaType.APPLICATION_PDF);
            }
            else throw new RuntimeException("El formato del archivo no es apto para retornarse");
            headers.setContentDisposition(ContentDisposition.attachment().filename(nombreArchivo).build());
            return new ResponseEntity<>(formato.getDocumento(), headers, HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @PostMapping()
    public ResponseEntity<?> crearFormato(@RequestParam String modulo, @RequestParam MultipartFile documento) {
        try {
            String extension = documento.getOriginalFilename().substring(documento.getOriginalFilename().lastIndexOf("."));
            Formato formato = new Formato(null, modulo, LocalDate.now(), documento.getBytes(), extension);
            this.formatoServices.guardarFormato(formato);
            return new ResponseEntity<ResponseConfirmation>(new ResponseConfirmation("El formato se guardo correctamente"), HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
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
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping("/listar")
    public ResponseEntity<?> listarFormatos() {
        try {
            return new ResponseEntity<List<Formato>>(this.formatoServices.obtenerListado(), HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }
    @GetMapping(value = "/IdeaNegocio")
    public ResponseEntity<?> recuperarFormatoIdeaNegocio() {
        try {
            Formato formato = this.formatoServices.obtenerFormatoIdea();
            if(formato == null) throw new RuntimeException("No hay formatos de idea de negocio cargados en el sistema");
            String nombreArchivo = this.formatoServices.obtenerNombre(formato.getId().toString());
                HttpHeaders headers = new HttpHeaders();
                if (formato.getExtension().equals(".docx")) {
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                }
                else if (formato.getExtension().equals(".pdf")) {
                    headers.setContentType(MediaType.APPLICATION_PDF);
                }
                else throw new RuntimeException("El formato del archivo no es apto para retornarse");
                headers.setContentDisposition(ContentDisposition.attachment().filename(nombreArchivo).build());
                return new ResponseEntity<>(formato.getDocumento(), headers, HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @GetMapping(value = "/PlanNegocio")
    public ResponseEntity<?> recuperarFormatoPlanNegocio() {
        try {
            Formato formato = this.formatoServices.obtenerFormatoPlan();
            if(formato == null) throw new RuntimeException("No hay formatos de plan de negocio cargados en el sistema");
            String nombreArchivo = this.formatoServices.obtenerNombre(formato.getId().toString());
            HttpHeaders headers = new HttpHeaders();
            if (formato.getExtension().equals(".docx")) {
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }
            else if (formato.getExtension().equals(".pdf")) {
                headers.setContentType(MediaType.APPLICATION_PDF);
            }
            else throw new RuntimeException("El formato del archivo no es apto para retornarse");
            headers.setContentDisposition(ContentDisposition.attachment().filename(nombreArchivo).build());
            return new ResponseEntity<>(formato.getDocumento(), headers, HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }


}
