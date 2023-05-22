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
                String htmlContent = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1\"><title>Correo SoftUE</title><style>body{margin:0;padding:0}.banner{background-color:#1c3b57;height:40px}.content{text-align:center;padding:20px}h1{color:#1c3b57;font-family:Arial,sans-serif;font-size:24px;margin:0;padding:0}p{font-family:Arial,sans-serif;font-size:14px;line-height:1.5;margin:10px 0;padding:0}.link{color:#1c3b57;text-decoration:none}img{max-width:100%;height:auto;max-height:300px}</style></head><body><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"background-color:#f2f2f2\"><tr><td align=\"center\"><table width=\"600\" cellspacing=\"0\" cellpadding=\"0\" style=\"background-color:#fff;border:1px solid #ddd\"><tr><td class=\"banner\"></td></tr><tr><td class=\"content\"><h1>Recuperación de contraseña</h1><p>Hola \uD83D\uDE0A</p><p>Recibimos una solicitud para recuperar la contraseña de tu cuenta. Haz clic en el enlace a continuación para restablecer tu contraseña:</p><p><a href=\"https://www.google.com\" class=\"link\">Restablecer Contraseña</a></p><p>Si no solicitaste restablecer tu contraseña, puedes ignorar este correo electrónico.</p><p>Atentamente,</p><p><i>El equipo de TopoSoft</i></p><img src=\"https://live.staticflickr.com/65535/52915619231_cf5898c387.jpg\" alt=\"Imagen\" style=\"max-width:500px;max-height:300px\"></td></tr><tr><td class=\"banner\"></td></tr></table></td></tr></table></body></html>";
                helper.setText(htmlContent, true);

                emailService.send(message);
            } catch (MessagingException e) {
                ResponseEntity.badRequest().body(new ResponseError(e.getMessage()));
            }
        });
    }
}


