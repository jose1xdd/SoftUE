package com.backend.softue.controllers;

import com.backend.softue.services.PeriodoServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.FormatoPeriodo;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/periodo")
public class PeriodoController {

    @Autowired
    private PeriodoServices periodoServices;

    @CheckSession(permitedRol ={"coordinador", "administrativo"})
    @PatchMapping()
    public ResponseEntity<?> actualizar(@RequestBody FormatoPeriodo periodo) {
        try {
            this.periodoServices.actualizar(periodo);
            return ResponseEntity.ok(new ResponseConfirmation("Se actualizó correctamente los periodos de idea de negocio y plan de negocio."));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol ={"coordinador", "administrativo"})
    @GetMapping()
    public ResponseEntity<?> obtener() {
        try {
            return ResponseEntity.ok(this.periodoServices.obtener());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

}
