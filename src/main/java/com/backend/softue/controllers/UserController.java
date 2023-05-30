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
    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @PostMapping("/guardarFoto")
    public ResponseEntity<?> guardarFoto(@RequestParam("foto") MultipartFile file, @RequestParam("correo") String correo) {
        try {
            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            if (!extension.equals(".jpg") && !extension.equals(".png"))
                throw new RuntimeException("Solo se permiten imagenes en formato .jpg o .png");
            return ResponseEntity.ok(userServices.guardarFoto(file.getBytes(), correo, extension));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("X-Softue-JWT") String jwt) {
        try {
            this.userServices.logout(jwt);
            return ResponseEntity.ok(new ResponseConfirmation("Cierre de Sesion Exitoso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @GetMapping("/forgotPassword/{email}")
    public ResponseEntity<?> forgotPassword(@PathVariable("email") String email) {
        try {
            return ResponseEntity.ok(new ResponseToken(this.userServices.forgotPassword(email)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"coordinador", "administrativo"})
    @PatchMapping("/update")
    public ResponseEntity<?> actualizar(@RequestHeader("X-Softue-JWT") String jwt, @Valid @RequestBody User user, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessages = errorFactory.errorGenerator(bindingResult);
                return ResponseEntity.badRequest().body(new ResponseError("Input Error",errorMessages,"Bad Request"));
            }
            this.userServices.actualizarUsuario(user, jwt);
            return ResponseEntity.ok(new ResponseConfirmation("El usuario ha sido actualizado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping("/{email}")
    public ResponseEntity<?> visualizar(@RequestHeader("X-Softue-JWT") String jwt, @PathVariable String email) {
        try {
            return ResponseEntity.ok(this.userServices.obtenerUsuario(email));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }
    @CheckSession(permitedRol = {"coordinador", "administrativo", "docente"})
    @PatchMapping("/resetPassword")
    public ResponseEntity resetPassword(@Valid @RequestHeader("X-Softue-Reset") String token, @RequestBody RequestPassword password) {
        try {
            this.userServices.resetPassword(token, password.getPassword());
            return ResponseEntity.ok(new ResponseConfirmation("Contraseña Restablecida"));
        } catch (Exception e) {

            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping(value = "/foto/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> obtenerFoto(@RequestHeader("X-Softue-JWT") String jwt, @PathVariable String id) {
        try {
            return ResponseEntity.ok(this.userServices.obtenerFoto(id));
        } catch (Exception e) {

            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"coordinador", "administrativo"})
    @GetMapping("/deshabilitarUsuario/{email}")
    public ResponseEntity<?> deshabilitarUsuario(@RequestHeader("X-Softue-JWT") String jwt, @PathVariable String email) {
        try {
            this.userServices.deshabilitarUsuario(email);
            return ResponseEntity.ok(new ResponseConfirmation("El usuario ha sido deshabilitado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"coordinador", "administrativo"})
    @GetMapping("/listar/{rol}")
    public ResponseEntity<?> listarUsers(@PathVariable String rol) {
        try {
            return ResponseEntity.ok(this.userServices.listarUsuariosRol(rol));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"coordinador", "administrativo"})
    @GetMapping("/asignar/{idea}/{docente}")
    public ResponseEntity<?> asignarTutor(@PathVariable String idea , @PathVariable String docente) {
        try {
            this.userServices.solicitarDocente(idea,docente);
            return ResponseEntity.ok(new ResponseConfirmation("Correo Enviado"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(), e.getMessage(), e.getStackTrace()[0].toString()));

        }
    }

}
