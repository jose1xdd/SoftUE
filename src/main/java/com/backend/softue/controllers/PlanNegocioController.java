package com.backend.softue.controllers;


import com.backend.softue.models.DocumentoPlan;
import com.backend.softue.models.PlanNegocio;
import com.backend.softue.services.PlanNegocioServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/planNegocio")
public class PlanNegocioController {

    @Autowired
    private PlanNegocioServices planNegocioServices;

    @CheckSession(permitedRol = {"coordinador"})
    @PostMapping("/{titulo}")
    public ResponseEntity<?> crear(@RequestHeader("X-Softue-JWT") String jwt, @PathVariable String titulo) {
        try {
            this.planNegocioServices.crear(jwt, titulo);
            return ResponseEntity.ok(new ResponseConfirmation("Plan de negocio creado correctamente"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"estudiante"})
    @PatchMapping()
    public ResponseEntity<?> actualizar(@RequestHeader("X-Softue-JWT") String jwt, @RequestParam String titulo, @RequestParam String resumen) {
        try {
            this.planNegocioServices.actualizarPlan(titulo, resumen, jwt);
            return ResponseEntity.ok(new ResponseConfirmation("El resumen del plan de negocio ha sido actualizado correctamente"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol ={"estudiante"})
    @PostMapping("/agregarDocumento")
    public ResponseEntity<?> agregarDocumento(@RequestParam String titulo, @RequestParam MultipartFile documento) {
        try {
            this.planNegocioServices.agregarDocumento(titulo, documento.getBytes(), documento.getOriginalFilename());
            return ResponseEntity.ok(new ResponseConfirmation("El documento del plan de negocio se agregó correctamete"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol ={"estudiante"})
    @DeleteMapping("/{titulo}")
    public ResponseEntity<?> eliminarDocumento(@PathVariable String titulo) {
        try {
            this.planNegocioServices.eliminarDocumento(titulo);
            return new ResponseEntity<ResponseConfirmation>(new ResponseConfirmation("El documento del plan de negocio se borró correctamente"), HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping(value = "/recuperarDocumento/{titulo}", produces = { "application/octet-stream", "application/pdf" })
    public ResponseEntity<?> recuperarDocumento(@PathVariable String titulo) {
        try {
            DocumentoPlan documentoPlan = this.planNegocioServices.recuperarDocumento(titulo);
            String extension = documentoPlan.getNombreArchivo().substring(documentoPlan.getNombreArchivo().lastIndexOf("."));
            HttpHeaders headers = new HttpHeaders();
            if (extension.equals(".docx")) {
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }
            else if (extension.equals(".pdf")) {
                headers.setContentType(MediaType.APPLICATION_PDF);
            }
            else throw new RuntimeException("El formato del archivo no es apto para retornarse");
            headers.setContentDisposition(ContentDisposition.attachment().filename(documentoPlan.getNombreArchivo()).build());
            return new ResponseEntity<>(documentoPlan.getDocumento(), headers, HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @PostMapping("/filtrar")
    public ResponseEntity<List<PlanNegocio>> buscarPlanesPorFiltros(
            @RequestParam(required = false)String tutorCodigo,
            @RequestParam(required = false) String codigoEstudiante,
            @RequestParam(required = false) String area,
            @RequestParam(required = false)  String estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<PlanNegocio> planesNegocio = this.planNegocioServices.buscarPlanPorFiltros(tutorCodigo, codigoEstudiante, area, estado, fechaInicio, fechaFin);
        return ResponseEntity.ok(planesNegocio);
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping("/{titulo}")
    public ResponseEntity<?> visualizar(@PathVariable String titulo) {
        try {
            return ResponseEntity.ok(this.planNegocioServices.obtenerPlanNegocio(titulo));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping()
    public ResponseEntity<?> listar() {
        try {
            return ResponseEntity.ok(this.planNegocioServices.listar());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(),e.getMessage(),e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @PostMapping("/PlanesDocentesApoyo")
    public ResponseEntity<?> listarPlanesDocenteApoyo(
            @RequestParam(required = false) Integer docenteCodigo,
            @RequestParam(required = false) Integer estudianteCodigo,
            @RequestParam(required = false) Integer area,
            @RequestParam(required = false) String estado){
        try {
            return ResponseEntity.ok(this.planNegocioServices.listarPlanesDocenteApoyo(docenteCodigo,estudianteCodigo,area,estado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(), e.getMessage(), e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @PostMapping("/PlanesDocentesEvaluadores")
    public ResponseEntity<?> listarPlanesDocenteEvaluador(
            @RequestParam(required = false) Integer docenteCodigo,
            @RequestParam(required = false) Integer estudianteCodigo,
            @RequestParam(required = false) Integer area,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin){
        try {
            return ResponseEntity.ok(this.planNegocioServices.listarPlanesDocenteEvaluador(docenteCodigo,estudianteCodigo,area,estado,fechaInicio,fechaFin));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(), e.getMessage(), e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping("/comprobarPlanAprobada")
    public ResponseEntity<?> comprobarPlanAprobado(@RequestParam String correoEstudiante){
        try {
            return ResponseEntity.ok(this.planNegocioServices.comprobarPlanAprobado(correoEstudiante));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }
}
