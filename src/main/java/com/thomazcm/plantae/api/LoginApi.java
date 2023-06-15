package com.thomazcm.plantae.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thomazcm.plantae.config.TokenService;
import com.thomazcm.plantae.dto.LoginForm;
import com.thomazcm.plantae.dto.TokenDto;

@RestController
@RequestMapping("/auth")
public class LoginApi {
    
    private AuthenticationManager authManager;
    private TokenService tokenService;
    
    public LoginApi(AuthenticationManager authManager, TokenService service) {
        this.authManager = authManager;
        this.tokenService = service;
    }

    @PostMapping
    public ResponseEntity<TokenDto> autenticar(@RequestBody LoginForm form) {
        
        System.out.println(1);
        System.out.println(form.getEmail());
        System.out.println(form.getSenha());
        UsernamePasswordAuthenticationToken userPassAuthToken = form.toUserPassAuthToken();
        
        try {
            Authentication authentication = authManager.authenticate(userPassAuthToken);
            String token = tokenService.gerarToken(authentication);
            return ResponseEntity.ok()
                    .body(new TokenDto(token, "Bearer"));
            
        } catch (AuthenticationException e) {
//            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("veganas2023"));
    }
    
}
