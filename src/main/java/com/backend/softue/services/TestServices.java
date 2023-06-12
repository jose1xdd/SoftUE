package com.backend.softue.services;

import com.backend.softue.models.*;
import com.backend.softue.repositories.TestRepository;
import com.backend.softue.utils.response.ComponenteValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class TestServices {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private RespuestaServices respuestaServices;

    @Autowired
    private EstudianteServices estudianteServices;

    @Autowired
    private RespuestaSeleccionadaServices respuestaSeleccionadaServices;

    public void crear(Integer codigoEstudiante, Integer respuestasId[], LocalDate fechaCreacion) {
        if (codigoEstudiante == null)
            throw new RuntimeException("No se puede asignar los resultados de una prueba sin un código de estudiante");
        if (respuestasId == null)
            throw new RuntimeException("No se puede asignar los resultados de una prueba sin los IDs de las respuestas");
        Estudiante estudiante = this.estudianteServices.obtenerEstudiante(codigoEstudiante);
        List<Respuesta> respuestas = new LinkedList<>();
        for (Integer id : respuestasId) {
            try {
                respuestas.add(this.respuestaServices.obtener(id));
            }
            catch (Exception e) {
                throw new RuntimeException("No existe una pregunta con ID " + id);
            }
        }
        Test test = new Test(null, estudiante, fechaCreacion, 0.0);
        this.testRepository.save(test);
        test = this.testRepository.findByEstudianteAndFecha(estudiante.getCodigo(), fechaCreacion);
        for (Respuesta respuesta : respuestas) {
            this.respuestaSeleccionadaServices.crear(test, respuesta);
        }
        test.setCalificacion(this.testRepository.obtenerResultado(test.getId()));
        this.testRepository.save(test);
        if(test.getCalificacion() >= 75){
            estudiante.setCapacitacionAprobada("aprobada");
            this.estudianteServices.actualizarEstudiante(estudiante);
        }
    }

    public List<Test> listar() {
        return this.testRepository.findAll();
    }

    public List<ComponenteValue> obtenerResultadosByTest(Integer testId) {
        if (testId == null)
            throw new RuntimeException("No se puede obtener con un testId igual a null");
        Test test = this.testRepository.findById(testId).get();
        if (test == null)
            throw new RuntimeException("No existe un test con ese ID");
        return this.testRepository.obtenerResultadoComponentes(testId);
    }

    public List<ComponenteValue> obtenerResultadosByEstudiante(Integer codigoEstudiante) {
        if (codigoEstudiante == null)
            throw new RuntimeException("No se puede obtener con un codigoEstudiante igual a null");
        Estudiante estudiante = this.estudianteServices.obtenerEstudiante(codigoEstudiante);
        if (estudiante == null)
            throw new RuntimeException("No existe un estudiante con ese codigo");
        return obtenerResultadosByTest(this.testRepository.obtenerUltimoTestEstudiante(codigoEstudiante));
    }
    public Optional<Test> obtenerResultadoTest(Integer codigoEstudiante) {
        if (codigoEstudiante == null)
            throw new RuntimeException("No se puede obtener con un codigoEstudiante igual a null");
        Estudiante estudiante = this.estudianteServices.obtenerEstudiante(codigoEstudiante);
        if (estudiante == null)
            throw new RuntimeException("No existe un estudiante con ese codigo");
        System.out.println(1);
        Integer codigoTest = this.testRepository.obtenerUltimoTestEstudiante(estudiante.getCodigo());
        System.out.println(2);
        return this.testRepository.findById(codigoTest);
    }
}
