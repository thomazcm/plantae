package com.thomazcm.plantae.api;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thomazcm.plantae.config.TokenService;
import com.thomazcm.plantae.dto.LoginForm;

@RestController
@RequestMapping("/auth")
public class LoginApi {
    
    private AuthenticationManager authManager;
    private TokenService tokenService;
    @Value("${rest.jwt.expiration}")
    private String jwtExpiration;
    
    public LoginApi(AuthenticationManager authManager, TokenService service) {
        this.authManager = authManager;
        this.tokenService = service;
    }

    @PostMapping
    public ResponseEntity<?> autenticar(@RequestBody LoginForm form, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken userPassAuthToken = form.toUserPassAuthToken();
        
        try {
            Authentication authentication = authManager.authenticate(userPassAuthToken);
            String token = tokenService.gerarToken(authentication);
            
            Cookie jwtCookie = new Cookie("JWT-TOKEN", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(Integer.parseInt(jwtExpiration)*3600);
            response.addCookie(jwtCookie);
            
            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
}
