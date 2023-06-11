package com.backend.softue.services;

import com.backend.softue.models.*;
import com.backend.softue.repositories.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

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
    }
}
