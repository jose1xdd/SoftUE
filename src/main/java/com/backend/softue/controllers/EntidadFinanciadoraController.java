package com.backend.softue.controllers;


import com.backend.softue.models.EntidadFinanciadora;
import com.backend.softue.models.FotoEntidadFinanciadora;
import com.backend.softue.models.FotoUsuario;
import com.backend.softue.services.EntidadFinanciadoraServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ErrorFactory;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
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
    public ResponseEntity<?> crear(@Valid @RequestBody EntidadFinanciadora entidadFinanciadora, BindingResult bindingResult) {
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
            this.entidadFinanciadoraServices.guardarFoto(correoEntidadFinanciadora, file);
            return ResponseEntity.ok("La foto se ha guardado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(), e.getMessage(), e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"coordinador"})
    @DeleteMapping()
    public ResponseEntity<?> eliminar(@RequestParam String correo) {
        try {
            this.entidadFinanciadoraServices.eliminar(correo);
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
    @GetMapping(value = "/foto/{email}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<?> visualizarFoto(@PathVariable String email) {
        try {
            FotoEntidadFinanciadora fotoEntidadFinanciadora = this.entidadFinanciadoraServices.visualizarFoto(email);
            HttpHeaders headers = new HttpHeaders();
            if (fotoEntidadFinanciadora.getExtension().equals(".jpg")) {
                headers.setContentType(MediaType.IMAGE_JPEG);
            } else if (fotoEntidadFinanciadora.getExtension().equals(".png")) {
                headers.setContentType(MediaType.IMAGE_PNG);
            } else throw new RuntimeException("El formato del archivo no es apto para retornarse");
            headers.setContentDisposition(ContentDisposition.attachment().filename("Foto entidad" + fotoEntidadFinanciadora.getId()).build());
            return new ResponseEntity<>(fotoEntidadFinanciadora.getFoto(), headers, HttpStatus.OK);
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
