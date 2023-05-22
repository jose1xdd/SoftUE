package com.backend.softue.utils.emailModule;

import com.backend.softue.utils.response.ResponseError;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Getter
@Service
public class EmailService {

    private JavaMailSender emailService;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.emailService = javaMailSender;
    }

    public void enviarCorreo(String destinatario, String asunto, String contenido) {
        CompletableFuture.runAsync(() -> {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(destinatario);
            mensaje.setSubject(asunto);
            mensaje.setText(contenido);
            emailService.send(mensaje);
        });
    }

    public void enviarEmailRegistro(String email) {
        CompletableFuture.runAsync(() -> {
            try {
                MimeMessage message = emailService.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setTo(email);
                helper.setSubject("Recuperación de contraseña");
                String htmlContent = "<html><body><h1>Recuperación de Contraseña</h1>" +
                        "<p>Hola,</p><p>Recibimos una solicitud para recuperar la contraseña de tu cuenta." +
                        " Haz clic en el enlace a continuación para restablecer tu contraseña:</p>" +
                        "<p><a href=" + "'>Restablecer Contraseña</a></p>" +
                        "<p>Si no solicitaste restablecer tu contraseña, puedes ignorar este correo electrónico.</p>" +
                        "<p>Atentamente,<br>El equipo de TopoSoft</p></body></html>";
                helper.setText(htmlContent, true);

                emailService.send(message);
            } catch (MessagingException e) {
                ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
            }
        });
    }
}

