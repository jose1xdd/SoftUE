package com.backend.softue.controllers;

import com.backend.softue.security.Hashing;
import com.backend.softue.services.UserServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/user")
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

    @PatchMapping("/resetPassword")
    public ResponseEntity resetPassword(@RequestHeader("X-Softue-Reset") String token, @RequestBody RequestPassword password) {
        try {
            this.userServices.resetPassword(token, password.getPassword());
            return ResponseEntity.ok(new ResponseConfirmation("Contraseña Restablecida"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
        }
    }

    @GetMapping("/test")
    public Boolean test(@RequestHeader("password")String password,@RequestHeader("nuevapass") String nuevaPasss){
        return this.encryp.validate(nuevaPasss,password);
    }

}
