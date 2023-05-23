package com.backend.softue.controllers;


import com.backend.softue.models.EntidadFinanciadora;
import com.backend.softue.services.EntidadFinanciadoraServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ErrorFactory;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/entidadFinanciadora")
public class EntidadFinanciadoraController {

    @Autowired
    private EntidadFinanciadoraServices entidadFinanciadoraServices;

    @Autowired
    private ErrorFactory errorFactory;

    @CheckSession(permitedRol = {"coordinador"})
    @PostMapping()
    public ResponseEntity<?> crear(@Valid @RequestBody EntidadFinanciadora entidadFinanciadora,
                                   BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessages = errorFactory.errorGenerator(bindingResult);
                return ResponseEntity.badRequest().body(new ResponseError("Input Error", errorMessages, "Bad Request"));
            }
            this.entidadFinanciadoraServices.crear(entidadFinanciadora);
            return ResponseEntity.ok(new ResponseConfirmation("Entidad Financiadora registrada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(), e.getMessage(), e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"coordinador"})
    @PatchMapping()
    public ResponseEntity<?> actualizar(@Valid @RequestBody EntidadFinanciadora entidadFinanciadora,
                                        BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessages = errorFactory.errorGenerator(bindingResult);
                return ResponseEntity.badRequest().body(new ResponseError("Input Error", errorMessages, "Bad Request"));
            }
            this.entidadFinanciadoraServices.actualizar(entidadFinanciadora);
            return ResponseEntity.ok(new ResponseConfirmation("Entidad Financiadora actualizada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(), e.getMessage(), e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"coordinador"})
    @PostMapping("/guardarFoto/{correoEntidadFinanciadora}")
    public ResponseEntity<?> guardarFoto(@RequestParam("foto") MultipartFile file,
                                         @PathVariable("correoEntidadFinanciadora") String correoEntidadFinanciadora) {
        try {
            return ResponseEntity.ok(this.entidadFinanciadoraServices.guardarFoto(file, correoEntidadFinanciadora));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(), e.getMessage(), e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"coordinador"})
    @DeleteMapping()
    public ResponseEntity<?> eliminar(@RequestParam("idEntidadFinanciadora") Integer idEntidadFinanciadora) {
        try {
            this.entidadFinanciadoraServices.eliminar(idEntidadFinanciadora);
            return ResponseEntity.ok(new ResponseConfirmation("Entidad Financiadora eliminada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(), e.getMessage(), e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping("/{email}")
    public ResponseEntity<?> visualizar(@PathVariable String email) {
        try {
            EntidadFinanciadora entidadFinanciadora = this.entidadFinanciadoraServices.visualizar(email);
            return ResponseEntity.ok(entidadFinanciadora);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(), e.getMessage(), e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping(value = "/foto/{email}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> visualizarFoto(@PathVariable String email) {
        try {
            byte[] foto = this.entidadFinanciadoraServices.visualizarFoto(email);
            return ResponseEntity.ok(foto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(), e.getMessage(), e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping()
    public ResponseEntity<?> listar() {
        try {
            return ResponseEntity.ok(this.entidadFinanciadoraServices.listar());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(), e.getMessage(), e.getStackTrace()[0].toString()));
        }
    }

}
