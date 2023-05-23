package com.backend.softue.services;

import com.backend.softue.models.Formato;
import com.backend.softue.repositories.FormatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class FormatoServices {

    @Autowired
    private FormatoRepository formatoRepository;

    public byte[] obtenerFormato(String id) throws SQLException {
        if (id != null) {
            Optional<Formato> result = this.formatoRepository.findById(Integer.parseInt(id));
            if (!result.isPresent()) throw new RuntimeException("El formato no existe");
            return result.get().getDocumento();
        }
        throw new RuntimeException("No se envió información con la que buscar el formato");
    }

    public String obtenerNombre(String id) {
        if (id != null) {
            Optional<Formato> result = this.formatoRepository.findById(Integer.parseInt(id));
            if (!result.isPresent()) throw new RuntimeException("El formato no existe");
            return result.get().getModulo() + "." + result.get().getExtension();
        }
        throw new RuntimeException("No se envió información con la que buscar el nombre del formato");
    }

    public void guardarFormato(Formato formato) {
        this.formatoRepository.save(formato);
    }

    public void borrarFormato(String id) {
        if (id != null) {
            Optional<Formato> result = this.formatoRepository.findById(Integer.parseInt(id));
            if (!result.isPresent()) throw new RuntimeException("El formato no existe");
            this.formatoRepository.delete(result.get());
        }
        else throw new RuntimeException("No se envió información con la que borrar el formato");
    }

    public List<Formato> obtenerListado() {
        List<Formato> result = this.formatoRepository.findAll();
        for (Formato formato : result) {
            formato.setDocumento(null);
        }
        return result;
    }
}
