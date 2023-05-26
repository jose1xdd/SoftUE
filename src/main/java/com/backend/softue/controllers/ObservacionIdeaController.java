package com.backend.softue.controllers;
import com.backend.softue.services.ObservacionIdeaServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/observacionIdea")
public class ObservacionIdeaController {
    @Autowired
    private ObservacionIdeaServices observacionIdeaServices;

    @CheckSession(permitedRol = {"docente"})
    @PostMapping()
    public ResponseEntity<?> crearObservacion(@RequestHeader("X-Softue-JWT") String jwt, @RequestParam String ideaTitulo, @RequestParam String observacion){
        try{
            this.observacionIdeaServices.crearObservacion(jwt,ideaTitulo,observacion);
            return ResponseEntity.ok(new ResponseConfirmation("El docente ha cargado la observacion exitosamente"));

        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"docente","estudiante","coordinador","administrativo"})
    @GetMapping("/{titulo}")
    public ResponseEntity<?> listarObservaciones(@RequestHeader("X-Softue-JWT") String jwt, @PathVariable("titulo") String titulo){
        try {
            return ResponseEntity.ok(this.observacionIdeaServices.obtenerObservaciones(jwt, titulo));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }



}
