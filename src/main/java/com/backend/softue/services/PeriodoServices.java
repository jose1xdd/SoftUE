package com.backend.softue.services;

import com.backend.softue.utils.beansAuxiliares.PeriodosDeEvaluacion;
import com.backend.softue.utils.FormatoPeriodo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Period;

@Service
public class PeriodoServices {

    @Autowired
    private PeriodosDeEvaluacion periodos;

    public void actualizar(FormatoPeriodo periodos) {
        if(periodos.getDiasPeriodoIdea() == null || periodos.getDiasPeriodoPlan() == null)
            throw new RuntimeException("Datos insuficientes para actualizar periodos de evaluación");
        if(periodos.getDiasPeriodoIdea() < 0 || periodos.getDiasPeriodoPlan() < 0)
            throw new RuntimeException("No se puede establecer un periodos de evaluación negativo");
        this.periodos.setPeriodoIdeaNegocio(Period.ofDays(periodos.getDiasPeriodoIdea()));
        this.periodos.setPeriodoPlanNegocio(Period.ofDays(periodos.getDiasPeriodoPlan()));
    }

    public PeriodosDeEvaluacion obtener() {
        return periodos;
    }
}
