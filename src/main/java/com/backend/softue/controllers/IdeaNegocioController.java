package com.backend.softue.controllers;

import com.backend.softue.models.DocumentoIdea;
import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.services.IdeaNegocioServices;
import com.backend.softue.services.IdeaPlanteadaServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ErrorFactory;
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
@RequestMapping("/ideaNegocio")
public class IdeaNegocioController {

    @Autowired
    private IdeaNegocioServices ideaNegocioServices;

    @Autowired
    private IdeaPlanteadaServices ideaPlanteadaServices;

    @Autowired
    private ErrorFactory errorFactory;

    @CheckSession(permitedRol = {"estudiante"})
    @PostMapping()
    public ResponseEntity<?> crear(@RequestHeader("X-Softue-JWT") String jwt, @RequestParam String titulo, @RequestParam String[] integrantes, @RequestParam String area, @RequestParam MultipartFile documento) {
        try {
            IdeaNegocio ideaNegocio = new IdeaNegocio(null, titulo, "formulado", null, null, null, null, LocalDate.now(), null, null, null, null, null, null, null, null,null,null);
            this.ideaNegocioServices.crear(ideaNegocio, area, integrantes, (!documento.isEmpty()) ? documento.getBytes() : null, (!documento.isEmpty()) ? documento.getOriginalFilename() : null, jwt);
            return ResponseEntity.ok(new ResponseConfirmation("Idea de negocio creada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(), e.getMessage(), e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"estudiante"})
    @PostMapping("/agregarDocumento")
    public ResponseEntity<?> agregarDocumento(@RequestParam String titulo, @RequestParam MultipartFile documento) {
        try {
            this.ideaNegocioServices.agregarDocumento(titulo, documento.getBytes(), documento.getOriginalFilename());
            return ResponseEntity.ok(new ResponseConfirmation("El formato de la idea de negocio se agrego correctamete"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(), e.getMessage(), e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"estudiante"})
    @DeleteMapping("/{titulo}")
    public ResponseEntity<?> eliminarDocumento(@PathVariable String titulo) {
        try {
            this.ideaNegocioServices.eliminarDocumento(titulo);
            return new ResponseEntity<ResponseConfirmation>(new ResponseConfirmation("El formato de la idea se borro correctamente"), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(), e.getMessage(), e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping(value = "/recuperarDocumento/{titulo}", produces = {"application/octet-stream", "application/pdf"})
    public ResponseEntity<?> recuperarDocumento(@PathVariable String titulo) {
        try {
            DocumentoIdea documentoIdea = this.ideaNegocioServices.recuperarDocumento(titulo);
            String extension = documentoIdea.getNombreArchivo().substring(documentoIdea.getNombreArchivo().lastIndexOf("."));
            HttpHeaders headers = new HttpHeaders();
            if (extension.equals(".docx")) {
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            } else if (extension.equals(".pdf")) {
                headers.setContentType(MediaType.APPLICATION_PDF);
            } else throw new RuntimeException("El formato del archivo no es apto para retornarse");
            headers.setContentDisposition(ContentDisposition.attachment().filename(documentoIdea.getNombreArchivo()).build());
            return new ResponseEntity<>(documentoIdea.getDocumento(), headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(), e.getMessage(), e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"estudiante"})
    @PatchMapping ("/Actualizar")
    public ResponseEntity<?> actualizar(@RequestHeader("X-Softue-JWT") String jwt,@RequestParam String tituloActual, @RequestParam String tituloNuevo, @RequestParam String area){
        try{
            this.ideaNegocioServices.actualizar(tituloActual,tituloNuevo, area.toLowerCase(), jwt);
            return ResponseEntity.ok(new ResponseConfirmation("Idea de negocio ha sido actualizada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(), e.getMessage(), e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"coordinador", "administrativo", "docente","estudiante"})
    @PostMapping("/filtrar")
    public ResponseEntity<List<IdeaNegocio>> buscarIdeasPorFiltros(
            @RequestParam(required = false)String tutorCodigo,
            @RequestParam(required = false) String codigoEstudiante,
            @RequestParam(required = false) String area,
            @RequestParam(required = false)  String estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<IdeaNegocio> ideasNegocio = this.ideaNegocioServices.buscarIdeasPorFiltros(tutorCodigo,codigoEstudiante ,area, estado, fechaInicio, fechaFin);
        return ResponseEntity.ok(ideasNegocio);
    }


    @GetMapping("/{titulo}")
    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    public ResponseEntity<?> visualizar(@PathVariable String titulo) {
        try {
            return ResponseEntity.ok(this.ideaNegocioServices.obtenerIdeaNegocio(titulo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(), e.getMessage(), e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @GetMapping()
    public ResponseEntity<?> listar() {
        try {
            return ResponseEntity.ok(this.ideaNegocioServices.listar());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(), e.getMessage(), e.getStackTrace()[0].toString()));
        }
    }


    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @PostMapping("/IdeasDocentesApoyo")
    public ResponseEntity<?> listarIdeasDocenteApoyo(
            @RequestParam(required = false) Integer docenteCodigo,
            @RequestParam(required = false) Integer estudianteCodigo,
            @RequestParam(required = false) Integer area,
            @RequestParam(required = false) String estado){
        try {
            return ResponseEntity.ok(this.ideaNegocioServices.listarIdeasDocenteApoyo(docenteCodigo,estudianteCodigo,area,estado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(), e.getMessage(), e.getStackTrace()[0].toString()));
        }
    }
    @CheckSession(permitedRol = {"estudiante", "coordinador", "administrativo", "docente"})
    @PostMapping("/IdeasDocentesEvaluadores")
    public ResponseEntity<?> listarIdeasDocenteEvaluador(
            @RequestParam(required = false) Integer docenteCodigo,
            @RequestParam(required = false) Integer estudianteCodigo,
            @RequestParam(required = false) Integer area,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin){
        try {
            return ResponseEntity.ok(this.ideaNegocioServices.listarIdeasDocenteEvaluador(docenteCodigo,estudianteCodigo,area,estado,fechaInicio,fechaFin));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e.getClass().toString(), e.getMessage(), e.getStackTrace()[0].toString()));
        }
    }

    @CheckSession(permitedRol = {"estudiante"})
    @GetMapping("/comprobarIdeaAprobada")
    public ResponseEntity<?> comprobarIdeaAprobada(@RequestParam String correoEstudiante){
        try {
            return ResponseEntity.ok(this.ideaNegocioServices.comprobarIdeaAprobada(correoEstudiante));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseError(e));
        }
    }
}

