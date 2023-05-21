package com.backend.softue.controllers;

import com.backend.softue.utils.emailModule.EmailGenericMessages;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailGenericMessages emailGenericMessages;

    @GetMapping("/")
    public ResponseEntity<?> sendEmail(@RequestHeader("email") String email) {
        try {
            this.emailGenericMessages.enviarEmailRegistro(email);
            return ResponseEntity.ok(new ResponseConfirmation("Email Enviado"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
        }
    }
}
