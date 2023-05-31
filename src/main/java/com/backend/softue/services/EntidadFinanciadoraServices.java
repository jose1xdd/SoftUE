package com.backend.softue.services;

import com.backend.softue.models.EntidadFinanciadora;
import com.backend.softue.models.FotoEntidadFinanciadora;
import com.backend.softue.models.FotoUsuario;
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
import java.util.List;

@Service
public class EntidadFinanciadoraServices {

    @Autowired
    private EntidadFinanciadoraRepository entidadFinanciadoraRepository;

    @Autowired
    private FotoEntidadFinanciadoraRepository fotoEntidadFinanciadoraRepository;

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

    public void guardarFoto(String correo, MultipartFile file) throws IOException {
        if (correo == null)
            throw new RuntimeException("Información insuficiente para guardar una foto de una entidad financiadora");
        if (file.isEmpty())
            throw new RuntimeException("No se envió un archivo para almacenar como foto de la entidad financiadora");
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        if (!extension.equals(".jpg") && !extension.equals(".png"))
            throw new RuntimeException("Las imagenes deben ser de extension .jpg o .png");
        EntidadFinanciadora resultado = this.entidadFinanciadoraRepository.findByCorreo(correo);
        if (resultado == null)
            throw new RuntimeException("La entidad financiadora a actualizar foto no existe");

        FotoEntidadFinanciadora fotoActual = resultado.getFotoEntidadFinanciadoraId();
        if (fotoActual != null) {
            resultado.setFotoEntidadFinanciadoraId(null);
            this.fotoEntidadFinanciadoraRepository.delete(fotoActual);
        }

        FotoEntidadFinanciadora fotoNueva = new FotoEntidadFinanciadora(null, file.getBytes(), extension, resultado);
        this.fotoEntidadFinanciadoraRepository.save(fotoNueva);
    }

    public void eliminar(String correo) {
        if(correo == null)
            throw new RuntimeException("Información insuficiente para eliminar una entidad financiadora");
        EntidadFinanciadora resultado = this.entidadFinanciadoraRepository.findByCorreo(correo);
        if(resultado == null) throw new RuntimeException("La entidad financiadora a eliminar no existe");
        this.entidadFinanciadoraRepository.delete(resultado);
    }

    public FotoEntidadFinanciadora visualizarFoto(String correo) {
        if(correo == null)
            throw new RuntimeException("Información insuficiente para visualizar la foto una entidad financiadora");

        EntidadFinanciadora entidadFinanciadora = this.entidadFinanciadoraRepository.findByCorreo(correo);
        if(entidadFinanciadora == null)
            throw new RuntimeException("La entidad financiadora consultada no existe");

        FotoEntidadFinanciadora fotoEntidadFinanciadora = this.fotoEntidadFinanciadoraRepository.findById(entidadFinanciadora.getId()).get();
        if(fotoEntidadFinanciadora == null)
            throw new RuntimeException("La entidad financiadora consultada no tiene foto");
        return fotoEntidadFinanciadora;
    }

    public List<EntidadFinanciadora> listar() {
        List<EntidadFinanciadora> entidadesFinanciadoras = this.entidadFinanciadoraRepository.findAll();
        if(entidadesFinanciadoras.isEmpty()) throw new RuntimeException("No hay entidades financiadoras");
        return entidadesFinanciadoras;
    }

    public EntidadFinanciadora visualizar(String correo) {
        if(correo == null)
            throw new RuntimeException("Información insuficiente para visualizar una entidad financiadora");
        EntidadFinanciadora entidadFinanciadora = this.entidadFinanciadoraRepository.findByCorreo(correo);
        if(entidadFinanciadora == null)
            throw new RuntimeException("La entidad financiadora consultada no existe");
        return entidadFinanciadora;
    }

}
