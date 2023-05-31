package com.backend.softue.services;

import com.backend.softue.models.Estudiante;
import com.backend.softue.models.EstudiantePlanKey;
import com.backend.softue.models.PlanNegocio;
import com.backend.softue.models.PlanPresentado;
import com.backend.softue.repositories.PlanPresentadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanPresentadoServices {
    @Autowired
    private PlanPresentadoRepository planPresentadoRepository;

    public void agregarIntegrante(PlanNegocio planNegocio, Estudiante estudiante){
        this.planPresentadoRepository.save(new PlanPresentado(new EstudiantePlanKey(estudiante.getCodigo(),planNegocio.getId()),estudiante,planNegocio));
    }
}
