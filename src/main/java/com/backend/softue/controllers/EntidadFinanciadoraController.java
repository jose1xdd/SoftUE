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
    @PostMapping("/guardarFoto/{correoEntidadFinanciadora}")
    public ResponseEntity<?> guardarFoto(@RequestParam("foto") MultipartFile file,
                                      @PathVariable("correoEntidadFinanciadora") String correoEntidadFinanciadora) {
        try {
            return ResponseEntity.ok(this.entidadFinanciadoraServices.guardarFoto(file, correoEntidadFinanciadora));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
        }
    }

    @CheckSession(permitedRol ={"coordinador"})
    @DeleteMapping()
    public ResponseEntity<?> eliminar(@RequestHeader("X-Softue-JWT") String jwt, @RequestParam("idEntidadFinanciadora") Integer idEntidadFinanciadora) {
        try {
            this.entidadFinanciadoraServices.eliminar(idEntidadFinanciadora);
            return new ResponseEntity<ResponseConfirmation>(new ResponseConfirmation("Entidad Financiadora eliminada correctamente"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseError>(new ResponseError(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
