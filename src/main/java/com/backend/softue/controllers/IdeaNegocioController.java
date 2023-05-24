package com.backend.softue.controllers;

import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.services.IdeaNegocioServices;
import com.backend.softue.services.IdeaPlanteadaServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ErrorFactory;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
            IdeaNegocio ideaNegocio = new IdeaNegocio(null, titulo, 'F', area, null, LocalDate.now(), null, null, null, null, null, null, null,null,null);
            this.ideaNegocioServices.crear(ideaNegocio, integrantes, documento.getBytes(), jwt);
            return ResponseEntity.ok(new ResponseConfirmation("Idea de negocio creada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol ={"estudiante"})
    @PostMapping("/agregarDocumento")
    public ResponseEntity<?> agregarFormato(@RequestParam String titulo, @RequestParam MultipartFile documento) {
        try {
            this.ideaNegocioServices.agregarDocumento(titulo, documento.getBytes());
            return ResponseEntity.ok(new ResponseConfirmation("El formato de la idea de negocio se agrego correctamete"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol ={"estudiante"})
    @DeleteMapping("/{titulo}")
    public ResponseEntity<?> eliminarFormato(@PathVariable String titulo) {
        try {
            this.ideaNegocioServices.eliminarDocumento(titulo);
            return new ResponseEntity<ResponseConfirmation>(new ResponseConfirmation("El formato de la idea se borro correctamente"), HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }
    @PatchMapping ("/Actualizar")
    public ResponseEntity<?> actualizar(@RequestHeader("X-Softue-JWT") String jwt,@RequestParam String tituloActual, @RequestParam String tituloNuevo, @RequestParam String area){
        try{
            this.ideaNegocioServices.actualizar(tituloActual,tituloNuevo, area.toLowerCase(), jwt);
            return ResponseEntity.ok(new ResponseConfirmation("Idea de negocio ha sido actualizada correctamente"));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @GetMapping("/{titulo}")
    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    public ResponseEntity<?> visualizar(@PathVariable String titulo) {
       try {
           return ResponseEntity.ok(this.ideaNegocioServices.obtenerIdeaNegocio(titulo));
       }
       catch (Exception e) {
           return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
       }
    }
}
