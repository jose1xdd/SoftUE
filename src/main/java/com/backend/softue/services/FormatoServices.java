package com.backend.softue.services;

import com.backend.softue.models.Formato;
import com.backend.softue.repositories.FormatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class FormatoServices {

    @Autowired
    private FormatoRepository formatoRepository;

    public byte[] obtenerFormato(String id) throws SQLException {
        if (id != null) {
            Formato result = this.formatoRepository.getReferenceById(Integer.parseInt(id));
            if(result == null) throw new RuntimeException("El formato no existe");
            return result.getDocumento().getBytes(1, (int) result.getDocumento().length());
        }
        throw new RuntimeException("No se envió información con la que buscar el formato");
    }

    public String obtenerNombre(String id) {
        if (id != null) {
            Formato result = this.formatoRepository.getReferenceById(Integer.parseInt(id));
            if(result == null) throw new RuntimeException("El formato no existe");
            return result.getModulo() + "." + result.getExtension();
        }
        throw new RuntimeException("No se envió información con la que buscar el nombre del formato");
    }
}
