package com.thomazcm.plantae.config;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.thomazcm.plantae.repository.UsuarioRepository;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter{
    
    private TokenService service;
    private UsuarioRepository repository;
    @Value("${rest.jwt.expiration}")
    private String jwtExpiration;
    
    public AutenticacaoViaTokenFilter(TokenService service, UsuarioRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        String token = getToken(request);

        if (token == null || !service.ehValido(token)) {
            //fail request
        }  else {
            if (service.isTokenExpiring(token)) {
                token = service.refreshToken(token);
                Cookie jwtCookie = new Cookie("JWT-TOKEN", token);  
                jwtCookie.setMaxAge(Integer.parseInt(jwtExpiration)* 60 * 60);  
                jwtCookie.setHttpOnly(true);
                jwtCookie.setPath("/");
                response.addCookie(jwtCookie);
            } 
            autenticaRequest(token);
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JWT-TOKEN")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return token;
    }

    private void autenticaRequest(String token) {
        var usuario = repository.findById(service.idFromToken(token)).get();
        var userPassAuthToken =
                new UsernamePasswordAuthenticationToken(usuario, null, usuario.getPerfis());
        SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);
    }

}
