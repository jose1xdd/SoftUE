package com.backend.softue.controllers;

import com.backend.softue.models.FotoUsuario;
import com.backend.softue.security.Hashing;
import com.backend.softue.services.PlanNegocioServicesInterface;
import com.backend.softue.services.UserServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.*;

import com.backend.softue.models.User;
import com.backend.softue.utils.response.ErrorFactory;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
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
    private PlanNegocioServicesInterface planNegocioServicesInterface;

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
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @GetMapping("/forgotPassword/{email}")
    public ResponseEntity<?> forgotPassword(@PathVariable("email") String email) {
        try {
            return ResponseEntity.ok(this.userServices.forgotPassword(email));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
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
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @PatchMapping("/updateCorreo")
    public ResponseEntity<?> actualizar(@RequestHeader("X-Softue-JWT") String jwt, @RequestParam Integer codigo, @RequestParam String correo) {
        try {
            this.userServices.actualizarUsuario(codigo, correo, jwt);
            return ResponseEntity.ok(new ResponseConfirmation("El usuario ha sido actualizado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }


    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping("/{email}")
    public ResponseEntity<?> visualizar(@RequestHeader("X-Softue-JWT") String jwt, @PathVariable String email) {
        try {
            return ResponseEntity.ok(this.userServices.obtenerUsuario(email));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @PatchMapping("/resetPassword")
    public ResponseEntity resetPassword(@Valid @RequestHeader("X-Softue-Reset") String token, @RequestBody RequestPassword password) {
        try {
            this.userServices.resetPassword(token, password.getPassword());
            return ResponseEntity.ok(new ResponseConfirmation("Contraseña Restablecida"));
        } catch (Exception e) {

            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"coordinador", "administrativo", "estudiante", "docente"})
    @PatchMapping("/reestablecer")
    public ResponseEntity restablecerContrasenia(@Valid @RequestHeader("X-Softue-JWT") String token, @RequestBody RequestPassword password) {
        try {
            this.userServices.restablecerContrasenia(token, password.getPassword());
            return ResponseEntity.ok(new ResponseConfirmation("Contraseña Restablecida"));
        } catch (Exception e) {

            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"coordinador", "administrativo"})
    @PatchMapping("/restablecerOtroUsuario")
    public ResponseEntity restablecerContraseniaOtroUsuario(@RequestParam String correo, @RequestParam String contrasenia) {
        try {
            this.userServices.restablecerContraseniaOtroUsuario(correo, contrasenia);
            return ResponseEntity.ok(new ResponseConfirmation("Contraseña Restablecida"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"coordinador", "administrativo"})
    @PostMapping("/eliminarTutor/{idea}")
    public ResponseEntity<?> eliminarTutor(@PathVariable String idea){
        try {
            this.userServices.borrarTutor(idea);
            return ResponseEntity.ok(new ResponseConfirmation("Tutor Borrado"));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping(value = "/foto/{id}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<?> obtenerFoto(@RequestHeader("X-Softue-JWT") String jwt, @PathVariable String id) {
        try {
            FotoUsuario fotoUsuario = this.userServices.obtenerFoto(id);
            HttpHeaders headers = new HttpHeaders();
            if (fotoUsuario.getExtension().equals(".jpg")) {
                headers.setContentType(MediaType.IMAGE_JPEG);
            } else if (fotoUsuario.getExtension().equals(".png")) {
                headers.setContentType(MediaType.IMAGE_PNG);
            } else throw new RuntimeException("El formato del archivo no es apto para retornarse");
            headers.setContentDisposition(ContentDisposition.attachment().filename("Foto usuario " + id).build());
            return new ResponseEntity<>(fotoUsuario.getFoto(), headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"coordinador", "administrativo"})
    @GetMapping("/deshabilitarUsuario/{email}")
    public ResponseEntity<?> deshabilitarUsuario(@RequestHeader("X-Softue-JWT") String jwt, @PathVariable String email) {
        try {
            this.userServices.deshabilitarUsuario(email);
            return ResponseEntity.ok(new ResponseConfirmation("El usuario ha sido deshabilitado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"coordinador", "administrativo"})
    @GetMapping("/listar/{rol}")
    public ResponseEntity<?> listarUsers(@PathVariable String rol) {
        try {
            return ResponseEntity.ok(this.userServices.listarUsuariosRol(rol));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }

    @CheckSession(permitedRol = {"coordinador", "administrativo"})
    @GetMapping("/asignar/{idea}/{docente}")
    public ResponseEntity<?> asignarTutor(@PathVariable String idea , @PathVariable String docente) {
        try {
            this.userServices.solicitarDocente(idea,docente);
            return ResponseEntity.ok(new ResponseConfirmation("Correo Enviado"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }
    @CheckSession(permitedRol = {"coordinador", "administrativo"})
    @GetMapping("/asignarPlan/{plan}/{docente}")
    public ResponseEntity<?> asignarTutorPlan(@PathVariable String plan , @PathVariable String docente) {
        try {
            this.planNegocioServicesInterface.asignarTutor(plan,docente);
            return ResponseEntity.ok(new ResponseConfirmation("Correo Enviado"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }
}

