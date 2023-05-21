package com.backend.softue.controllers;


import com.backend.softue.models.EntidadFinanciadora;
import com.backend.softue.services.EntidadFinanciadoraServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ErrorFactory;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @CheckSession(permitedRol ={"coordinador"})
    @PostMapping()
    public ResponseEntity<?> crear(@RequestHeader("X-Softue-JWT") String jwt, @Valid @RequestBody EntidadFinanciadora entidadFinanciadora, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessages = errorFactory.errorGenerator(bindingResult);
                return new ResponseEntity<ResponseError>(new ResponseError(errorMessages), HttpStatus.BAD_REQUEST);
            }
            this.entidadFinanciadoraServices.crear(entidadFinanciadora);
            return new ResponseEntity<ResponseConfirmation>(new ResponseConfirmation("Entidad Financiadora registrada correctamente"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseError>(new ResponseError(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @CheckSession(permitedRol ={"coordinador"})
    @PatchMapping()
    public ResponseEntity<?> actualizar(@RequestHeader("X-Softue-JWT") String jwt, @Valid @RequestBody EntidadFinanciadora entidadFinanciadora, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessages = errorFactory.errorGenerator(bindingResult);
                return new ResponseEntity<ResponseError>(new ResponseError(errorMessages), HttpStatus.BAD_REQUEST);
            }
            this.entidadFinanciadoraServices.actualizar(entidadFinanciadora);
            return new ResponseEntity<ResponseConfirmation>(new ResponseConfirmation("Entidad Financiadora actualizada correctamente"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseError>(new ResponseError(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @CheckSession(permitedRol ={"coordinador"})
    @PostMapping("/saveFoto/{entidadFinanciadoraId}")
    public ResponseEntity<?> guardarFoto(@RequestParam("photo") MultipartFile file,
                                      @PathVariable("entidadFinanciadoraId") Integer entidadFinanciadoraId) {
        try {
            return ResponseEntity.ok(this.entidadFinanciadoraServices.guardarFoto(file, entidadFinanciadoraId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
        }
    }
}
