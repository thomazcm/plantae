package com.thomazcm.plantae.config;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.thomazcm.plantae.repository.UsuarioRepository;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter{
    
    private TokenService service;
    private UsuarioRepository repository;
    
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
                Cookie freshCookie = service.createCookie(token);
                response.addCookie(freshCookie);
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
        var usuario = service.usuarioFromToken(token, repository);
        var userPassAuthToken =
                new UsernamePasswordAuthenticationToken(usuario, null, usuario.getPerfis());
        SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);
    }

}
