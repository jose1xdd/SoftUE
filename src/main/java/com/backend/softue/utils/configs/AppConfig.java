package com.backend.softue.utils.configs;

import com.backend.softue.models.ResetToken;
import com.backend.softue.models.SingInToken;
import com.backend.softue.repositories.ResetTokenRepository;
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
    @Autowired
    private ResetTokenRepository resetTokenRepository;
    @Scheduled(fixedRate = 28800000) // Se ejecuta cada día (en milisegundos)
    public void autodestruccion() {
        LocalDateTime nowMinusOneDay = LocalDateTime.now();
        List<SingInToken> tokensToDelete = singInTokenRepository.searchTokensByDate(nowMinusOneDay);
        singInTokenRepository.deleteAll(tokensToDelete);
        List<ResetToken> resetTokens = resetTokenRepository.searchTokensByDate(nowMinusOneDay);
        resetTokenRepository.deleteAll(resetTokens);
        System.out.println("delete tokens");
    }



}
