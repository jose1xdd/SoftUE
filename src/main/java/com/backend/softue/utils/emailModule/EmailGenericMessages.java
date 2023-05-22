package com.backend.softue.utils.emailModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class EmailGenericMessages {
    @Autowired
    private EmailService emailService;
    public void enviarEmailRegistro(String email){
        this.emailService.enviarCorreo(email,"Tanuki","https://localhost:8080/register");
    }
}
