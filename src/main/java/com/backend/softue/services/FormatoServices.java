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

    public Formato obtenerFormato(String id) {
        if (id != null) {
            Optional<Formato> result = this.formatoRepository.findById(Integer.parseInt(id));
            if (!result.isPresent()) throw new RuntimeException("El formato no existe");
            return result.get();
        }
        throw new RuntimeException("No se envió información con la que buscar el formato");
    }

    public Formato obtenerFormatoIdea() {
            return this.formatoRepository.FormatoRecienteIdea();
    }

    public Formato obtenerFormatoPlan() {
        return this.formatoRepository.FormatoRecientePlan();
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
        if (formato == null)
            throw new RuntimeException("No se envió ningún formato para guardar");
        if (!formato.getExtension().equals(".pdf") && !formato.getExtension().equals(".docx"))
            throw new RuntimeException("La extensión del archivo no es permitida, por favor subir documentos .docx o .pdf");
        if (!formato.getModulo().equals("idea_de_negocio") && !formato.getModulo().equals("plan_de_negocio")
                && !formato.getModulo().equals("material_idea_negocio") && !formato.getModulo().equals("material_plan_negocio")
        &&!formato.getModulo().equals("material_general"))
            throw new RuntimeException("El formato debe corresponder a idea_de_negocio o plan_de_negocio o material_idea_negocio o material_plan_negocio o material_general");
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

    public List<Formato> obtenerListadoFormatos() {
        List<Formato> result = this.formatoRepository.obtenerFormatoPlantillla();
        for (Formato formato : result) {
            formato.setDocumento(null);
        }
        return result;
    }

    public List<Formato> obtenerMaterialIdea(){
        List<Formato> result = this.formatoRepository.obtenerMaterialIdea();
        for (Formato formato : result) {
            formato.setDocumento(null);
        }
        return result;
    }

    public List<Formato> obtenerMaterialplan(){
        List<Formato> result = this.formatoRepository.obtenerMaterialPlan();
        for (Formato formato : result) {
            formato.setDocumento(null);
        }
        return result;
    }

    public List<Formato> obtenerMaterial(){
        List<Formato> result = this.formatoRepository.obtenerMaterial();
        for (Formato formato : result) {
            formato.setDocumento(null);
        }
        return result;
    }

    public List<Formato> obtenerMaterialGeneral(){
        List<Formato> result = this.formatoRepository.obtenerMaterialGeneral();
        for (Formato formato : result) {
            formato.setDocumento(null);
        }
        return result;
    }
}
