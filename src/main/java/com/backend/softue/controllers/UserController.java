package com.backend.softue.controllers;


import com.backend.softue.security.Hashing;
import com.backend.softue.services.UserServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.*;

import com.backend.softue.models.User;
import com.backend.softue.utils.response.ErrorFactory;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private Hashing encryp;
    @PostMapping("/saveFoto/{userId}")
    public ResponseEntity<?> saveFoto(@RequestParam("photo") MultipartFile file,
                                      @PathVariable("userId") String userId) {
        try {
            return ResponseEntity.ok(userServices.savePicture(file, userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("X-Softue-JWT") String jwt) {
        try {
            this.userServices.logout(jwt);
            return ResponseEntity.ok(new ResponseConfirmation("Cierre de Sesion Exitoso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
        }
    }

    @GetMapping("/forgotPassword/{email}")
    public ResponseEntity<?> forgotPassword(@PathVariable("email") String email) {
        try {
            return ResponseEntity.ok(new ResponseToken(this.userServices.forgotPassword(email)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
        }
    }

    @CheckSession(permitedRol ={"coordinador", "administrativo"})
    @PatchMapping("/update")
    public ResponseEntity<?> actualizar(@RequestHeader("X-Softue-JWT") String jwt, @Valid @RequestBody User user, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessages = errorFactory.errorGenerator(bindingResult);
                return new ResponseEntity<ResponseError>(new ResponseError(errorMessages), HttpStatus.BAD_REQUEST);
            }
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

    @PatchMapping("/resetPassword")
    public ResponseEntity resetPassword(@RequestHeader("X-Softue-Reset") String token, @RequestBody RequestPassword password) {
        try {
            this.userServices.resetPassword(token, password.getPassword());
            return ResponseEntity.ok(new ResponseConfirmation("Contraseña Restablecida"));
        } catch (Exception e) {

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

    @GetMapping("/test")
    public Boolean test(@RequestHeader("password")String password,@RequestHeader("nuevapass") String nuevaPasss){
        return this.encryp.validate(nuevaPasss,password);
    }


    @CheckSession(permitedRol ={"coordinador", "administrativo"})
    @GetMapping("/deshabilitarUsuario/{email}")
    public ResponseEntity<?> deshabilitarUsuario(@RequestHeader("X-Softue-JWT") String jwt, @PathVariable String email) {
        try {
            this.userServices.deshabilitarUsuario(email);
            return ResponseEntity.ok(new ResponseConfirmation("El usuario ha sido deshabilitado correctamente"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
        }
    }
}
