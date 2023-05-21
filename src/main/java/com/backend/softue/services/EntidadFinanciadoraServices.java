package com.backend.softue.services;

import com.backend.softue.models.EntidadFinanciadora;
import com.backend.softue.repositories.EntidadFinanciadoraRepository;
import com.backend.softue.security.Hashing;
import com.backend.softue.security.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

@Service
public class EntidadFinanciadoraServices {

    @Autowired
    private Roles roles;

    @Autowired
    private EntidadFinanciadoraRepository entidadFinanciadoraRepository;

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

    public String guardarFoto(MultipartFile file, Integer id) throws IOException, SQLException {

        return "Guardado";
    }
}
