package com.backend.softue.services;

import com.backend.softue.models.ComponenteCompetencias;
import com.backend.softue.repositories.ComponenteCompetenciasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComponenteCompetenciasServices {

    @Autowired
    private ComponenteCompetenciasRepository componenteCompetenciasRepository;

    private final double epsilon = 1e-9;

    public void crear(ComponenteCompetencias componenteCompetencias) {
        ComponenteCompetencias resultado = this.componenteCompetenciasRepository.findByNombre(componenteCompetencias.getNombre());
        if (resultado != null)
            throw new RuntimeException("El componente de competencias ya existe");
        if (!validarPorcentajePorArriba(componenteCompetencias.getValorPorcentaje()))
<<<<<<< HEAD
            throw new RuntimeException("El sumatorio de los porcentajes no puede ser mayor al 100%");
=======
            throw new RuntimeException("El sumaorio de los porcentajes no puede ser mayor al 100%");
>>>>>>> 67b2fde6d0c895f8ca0d6c0d33ec229f83c8eec6
        this.componenteCompetenciasRepository.save(componenteCompetencias);
    }

    public void actualizar(ComponenteCompetencias componenteCompetencias) {
        if (componenteCompetencias == null)
<<<<<<< HEAD
            throw new RuntimeException("El componente es nulo");
        ComponenteCompetencias resultado = this.componenteCompetenciasRepository.findById(componenteCompetencias.getId()).get();
        if (resultado == null)
            throw new RuntimeException("El componente de competencias a actualizar no existe");
=======
            throw new RuntimeException("El componente de competencias a actualizar no existe");
        ComponenteCompetencias resultado = this.componenteCompetenciasRepository.findById(componenteCompetencias.getId()).get();
>>>>>>> 67b2fde6d0c895f8ca0d6c0d33ec229f83c8eec6
        if (!validarPorcentajePorArriba(componenteCompetencias.getValorPorcentaje() - resultado.getValorPorcentaje()))
            throw new RuntimeException("Los porcentajes no pueden exceder del 100%");
        this.componenteCompetenciasRepository.save(componenteCompetencias);
    }

    public void eliminar(String nombre) {
        if (nombre == null)
            throw new RuntimeException("No se tiene un nombre con el que eliminar el componente");
        ComponenteCompetencias resultado = this.componenteCompetenciasRepository.findByNombre(nombre);
        if (resultado == null)
            throw new RuntimeException("El componente con ese nombre no existe");
        this.componenteCompetenciasRepository.delete(resultado);
    }

    public List<ComponenteCompetencias> listar() {
        List<ComponenteCompetencias> componenteCompetencias = this.componenteCompetenciasRepository.findAll();
        return componenteCompetencias;
    }

<<<<<<< HEAD
    public ComponenteCompetencias obtener(String nombre) {
        ComponenteCompetencias resultado = this.componenteCompetenciasRepository.findByNombre(nombre);
        if (resultado == null)
            throw new RuntimeException("La componente con ese nombre no existe");
        return resultado;
    }

=======
>>>>>>> 67b2fde6d0c895f8ca0d6c0d33ec229f83c8eec6
    public boolean validarPorcentaje() {
        return validarPorcentaje(0.0);
    }

    public boolean validarPorcentaje(double nuevoPorcentaje) {
        List<ComponenteCompetencias> resultado = listar();
        double sumatoria = nuevoPorcentaje;
        for (ComponenteCompetencias elemento : resultado) {
            sumatoria = sumatoria + elemento.getValorPorcentaje();
        }
        return Math.abs(100.0 - sumatoria) < epsilon;
    }

    public boolean validarPorcentajePorArriba(double nuevoPorcentaje) {
        List<ComponenteCompetencias> resultado = listar();
        double sumatoria = nuevoPorcentaje;
        for (ComponenteCompetencias elemento : resultado) {
            sumatoria = sumatoria + elemento.getValorPorcentaje();
        }
        return 100.0 - sumatoria > -epsilon;
    }
<<<<<<< HEAD

    public boolean existe(Integer id) {
        return this.componenteCompetenciasRepository.existsById(id);
    }
=======
>>>>>>> 67b2fde6d0c895f8ca0d6c0d33ec229f83c8eec6
}
