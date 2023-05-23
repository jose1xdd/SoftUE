package com.backend.softue.controllers;


import com.backend.softue.models.Estudiante;
import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.services.IdeaNegocioServices;
import com.backend.softue.services.IdeaPlanteadaServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ErrorFactory;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> crear(@RequestHeader("X-Softue-JWT") String jwt,
                                   @Valid @RequestBody IdeaNegocio ideaNegocio,
                                   BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessages = errorFactory.errorGenerator(bindingResult);
                return ResponseEntity.badRequest().body(new ResponseError("Input Error",errorMessages,"Bad Request"));
            }
            this.ideaNegocioServices.crear(ideaNegocio, jwt);
            this.ideaPlanteadaServices.crear(ideaNegocio);
            return ResponseEntity.ok(new ResponseConfirmation("Idea de negocio creada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol ={"estudiante"})
    @PatchMapping("/titulo")
    public ResponseEntity<?> actualizarTitulo(@RequestHeader("X-Softue-JWT") String jwt,
                                              @Valid @RequestBody IdeaNegocio ideaNegocio,
                                              BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessages = errorFactory.errorGenerator(bindingResult);
                return ResponseEntity.badRequest().body(new ResponseError("Input Error",errorMessages,"Bad Request"));
            }
            this.ideaNegocioServices.actualizarTitulo(ideaNegocio, jwt);
            return ResponseEntity.ok(new ResponseConfirmation("Título de la idea de negocio actualizado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol ={"docente"})
    @PatchMapping("/agregarIntegrante/{titulo}")
    public ResponseEntity<?> agregarIntegrante(@RequestHeader("X-Softue-JWT") String jwt,
                                               @Valid @RequestBody Estudiante estudiante,
                                               BindingResult bindingResult, @PathVariable String titulo) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessages = errorFactory.errorGenerator(bindingResult);
                return ResponseEntity.badRequest().body(new ResponseError("Input Error",errorMessages,"Bad Request"));
            }
            this.ideaNegocioServices.agregarIntegrante(estudiante, titulo, jwt);
            return ResponseEntity.ok(new ResponseConfirmation("Estudiante agregado a la idea de negocio correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol ={"docente"})
    @DeleteMapping("/eliminarIntegrante/{titulo}")
    public ResponseEntity<?> eliminarIntegrante(@RequestHeader("X-Softue-JWT") String jwt,
                                               @Valid @RequestBody Estudiante estudiante,
                                               BindingResult bindingResult, @PathVariable String titulo) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessages = errorFactory.errorGenerator(bindingResult);
                return ResponseEntity.badRequest().body(new ResponseError("Input Error",errorMessages,"Bad Request"));
            }
            this.ideaNegocioServices.eliminarIntegrante(estudiante, titulo, jwt);
            return ResponseEntity.ok(new ResponseConfirmation("Estudiante eliminado de la idea de negocio correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }
}
