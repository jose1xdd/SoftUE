package com.backend.softue.utils;

import com.backend.softue.models.SingInToken;
import com.backend.softue.repositories.SingInTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;


import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class AppConfig {
    @Autowired
    private SingInTokenRepository singInTokenRepository;
    @Scheduled(fixedRate = 86400000) // Se ejecuta cada día (en milisegundos)
    public void autodestruccion() {
        LocalDateTime nowMinusOneDay = LocalDateTime.now();
        List<SingInToken> tokensToDelete = singInTokenRepository.searchTokensByDate(nowMinusOneDay);
        singInTokenRepository.deleteAll(tokensToDelete);
        System.out.println("delete tokens");
    }


}
