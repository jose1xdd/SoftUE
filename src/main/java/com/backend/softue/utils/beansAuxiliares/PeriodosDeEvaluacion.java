package com.backend.softue.utils.beansAuxiliares;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.Period;

@Component
@Getter
@Setter
public class PeriodosDeEvaluacion {
    Period periodoIdeaNegocio;
    Period periodoPlanNegocio;

    public PeriodosDeEvaluacion() {
        this.periodoIdeaNegocio = Period.ofDays(30);
        this.periodoPlanNegocio = Period.ofDays(30);
    }
}
