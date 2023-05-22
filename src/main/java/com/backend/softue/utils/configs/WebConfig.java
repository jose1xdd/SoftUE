package com.backend.softue.utils.configs;

import com.backend.softue.utils.checkSession.CheckSessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private CheckSessionInterceptor checkSessionInterceptor;

    @Autowired
    public WebConfig(CheckSessionInterceptor checkSessionInterceptor){
        this.checkSessionInterceptor=checkSessionInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkSessionInterceptor);
    }

}
