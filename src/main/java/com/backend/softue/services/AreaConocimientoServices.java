package com.backend.softue.services;

import com.backend.softue.models.AreaConocimiento;
import com.backend.softue.repositories.AreaConocimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaConocimientoServices {
    @Autowired
    private AreaConocimientoRepository areaConocimientoRepository;

    public List<AreaConocimiento> listar() {
        return areaConocimientoRepository.findAll();
    }

    public void agregar(String nombre) {
        nombre = nombre.toLowerCase();
        if (nombre == null)
            throw new RuntimeException("No se paso un nombre con el que crear el area del conocimiento");
        if (this.areaConocimientoRepository.findByNombre(nombre) != null)
            throw new RuntimeException("Ya existe un area del conocimiento con ese nombre");
        this.areaConocimientoRepository.save(new AreaConocimiento(null, nombre, null, null));
    }

    public void eliminar(String nombre) {
        nombre = nombre.toLowerCase();
        if (nombre == null)
            throw new RuntimeException("No se paso un nombre con el que eliminar el area del conocimiento");
        AreaConocimiento areaConocimiento = this.areaConocimientoRepository.findByNombre(nombre);
        if (areaConocimiento == null)
            throw new RuntimeException("No existe un area del conocimiento con ese nombre");
        this.areaConocimientoRepository.delete(areaConocimiento);
    }

    public boolean existe(String nombre) {
        nombre = nombre.toLowerCase();
        return this.areaConocimientoRepository.findByNombre(nombre) != null;
    }

    public AreaConocimiento obtener(String nombre) {
        AreaConocimiento area = this.areaConocimientoRepository.findByNombre(nombre);
        if(area == null)
            throw new RuntimeException("El area del conocimiento no existe en el sistema.");
        return area;
    }
}
