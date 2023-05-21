package com.backend.softue.controllers;

import com.backend.softue.models.User;
import com.backend.softue.services.UserServices;
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
@RequestMapping(value = {"/coordinador", "/administrativo"})
public class UserController {
    @Autowired
    private UserServices userServices;
    @Autowired
    private ErrorFactory errorFactory;

    @PostMapping("/saveFoto/{userId}")
    public ResponseEntity<?> saveFoto(@RequestParam("photo") MultipartFile file,
                                      @PathVariable("userId") Integer userId) {
        try {
            return ResponseEntity.ok(userServices.savePicture(file, userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
        }
    }
    @CheckSession(permitedRol ={"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("X-Softue-JWT") String jwt) {
        try {
            this.userServices.logout(jwt);
            return ResponseEntity.ok(new ResponseConfirmation("Cierre de Sesion Exitoso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
        }
    }

    @CheckSession(permitedRol ={"coordinador", "administrativo"})
    @PatchMapping("/update")
    public ResponseEntity<?> actualizar(@RequestHeader("X-Softue-JWT") String jwt, @Valid @RequestBody User user, BindingResult bindingResult) {
        try {
            this.userServices.actualizarUsuario(user, jwt);
            return ResponseEntity.ok(new ResponseConfirmation("El usuario ha sido actualizado correctamente"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
        }
    }
    @CheckSession(permitedRol ={"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping("/{email}")
    public ResponseEntity<?> visualizar(@RequestHeader("X-Softue-JWT") String jwt, @PathVariable String email) {
        try {
            return ResponseEntity.ok(this.userServices.obtenerUsuario(email));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
        }
    }

    @CheckSession(permitedRol ={"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping(value = "/foto/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> obtenerFoto(@RequestHeader("X-Softue-JWT") String jwt, @PathVariable String id) {
        try {
            return ResponseEntity.ok(this.userServices.obtenerFoto(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
        }
    }
}
