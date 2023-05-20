package com.backend.softue.utils.checkSession;

import com.backend.softue.models.SingInToken;
import com.backend.softue.repositories.SingInTokenRepository;
import com.backend.softue.security.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CheckSessionInterceptor implements HandlerInterceptor {
    @Autowired
    SingInTokenRepository query;
    @Autowired
    JWTUtil jwt;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            CheckSession roles = handlerMethod.getMethodAnnotation(CheckSession.class);
            if (roles != null) {
                String headerName = "X-Softue-JWT";
                String headerActualName = request.getHeader(headerName);
                System.out.println(headerActualName);
                if (headerActualName == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Header name invalido");
                    return false;
                }
                SingInToken token = query.findByToken(headerActualName);
                if (token == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token Invalido");
                    return false;
                }
                if (!exist(roles.permitedRol(), headerActualName)) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Permisos Insuficientes");
                    return false;
                }
                System.out.println(sessionExpired(headerActualName));
                if (!sessionExpired(headerActualName)) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Logout");
                    return false;
                }
                return true;
            }
        }
        return true;
    }

    private boolean sessionExpired(String token) {
        if (jwt.isTokenExpired(token)) return false;
        List<SingInToken> tokensToDelete = this.query.findAll();
        if(tokensToDelete.isEmpty()) return false;
        for (SingInToken tokens : tokensToDelete) {
            if (tokens.getToken().equals(token)) return false;
        }
        return true;
    }


    private boolean exist(String roles[], String token) {
        String data = jwt.getValue(token);
        for (String role : roles) {
            if (data.equals(role)) {
                return true;
            }
        }
        return false;
    }
}
