package com.backend.softue.controllers;

import com.backend.softue.services.UserServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ErrorFactory;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;

@RestController
@RequestMapping("/user")
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

}
