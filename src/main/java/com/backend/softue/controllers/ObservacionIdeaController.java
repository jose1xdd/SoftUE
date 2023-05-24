package com.backend.softue.controllers;
import com.backend.softue.utils.checkSession.CheckSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/observacionIdea")
public class ObservacionIdeaController {

    @CheckSession(permitedRol = {"docente"})
    @PostMapping()
    public ResponseEntity<?> crearObservacion(@RequestHeader("X-Softue-JWT") String jwt, @RequestParam Integer correoDocente, @RequestParam Integer ideaTitulo, @RequestParam String observacion){
        try{

        }catch (Exception e){

        }
    }
}
