package com.backend.softue.services;

import com.backend.softue.models.EntidadFinanciadora;
import com.backend.softue.models.FotoEntidadFinanciadora;
import com.backend.softue.repositories.EntidadFinanciadoraRepository;
import com.backend.softue.repositories.FotoEntidadFinanciadoraRepository;
import com.backend.softue.security.Hashing;
import com.backend.softue.security.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

@Service
public class EntidadFinanciadoraServices {

    @Autowired
    private Roles roles;

    @Autowired
    private EntidadFinanciadoraRepository entidadFinanciadoraRepository;

    @Autowired
    private FotoEntidadFinanciadoraRepository fotoEntidadFinanciadoraRepository;

    @Autowired
    private Hashing encrypt;

    public void crear(EntidadFinanciadora entidadFinanciadora) {
        EntidadFinanciadora resultado = this.entidadFinanciadoraRepository.findByCorreo(entidadFinanciadora.getCorreo());
        if(resultado != null) throw new RuntimeException("La entidad financiadora ya existe.");
        this.entidadFinanciadoraRepository.save(entidadFinanciadora);
    }

    public void actualizar(EntidadFinanciadora entidadFinanciadora) {
        EntidadFinanciadora resultado = this.entidadFinanciadoraRepository.findByCorreo(entidadFinanciadora.getCorreo());
        if(resultado == null) throw new RuntimeException("La entidad financiadora a actualizar no existe");
        entidadFinanciadora.setId(resultado.getId());
        this.entidadFinanciadoraRepository.save(entidadFinanciadora);
    }

    public String guardarFoto(MultipartFile archivo, String correo) throws IOException, SQLException {
        EntidadFinanciadora resultado = this.entidadFinanciadoraRepository.findByCorreo(correo);
        if(resultado == null) throw new RuntimeException("La entidad financiadora a actualizar foto no existe");

        FotoEntidadFinanciadora fotoActual = resultado.getFotoEntidadFinanciadoraId();
        if(fotoActual != null) {
            resultado.setFotoEntidadFinanciadoraId(null);
            fotoActual.setEntidadFinanciadoraId(null);
            this.fotoEntidadFinanciadoraRepository.delete(fotoActual);
        }

        Blob blob = new SerialBlob(archivo.getBytes());
        FotoEntidadFinanciadora fotoNueva = new FotoEntidadFinanciadora(null, blob, resultado);
        this.fotoEntidadFinanciadoraRepository.save(fotoNueva);

        resultado.setFotoEntidadFinanciadoraId(fotoNueva);
        this.entidadFinanciadoraRepository.save(resultado);

        return "Guardado";
    }

    public void eliminar(Integer idEntidadFinanciadora) {
        EntidadFinanciadora resultado = this.entidadFinanciadoraRepository.findById(idEntidadFinanciadora).get();
        if(resultado == null) throw new RuntimeException("La entidad financiadora a eliminar no existe");
        this.entidadFinanciadoraRepository.delete(resultado);
    }

}
