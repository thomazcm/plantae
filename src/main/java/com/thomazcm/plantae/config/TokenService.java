package com.thomazcm.plantae.config;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import com.thomazcm.plantae.model.config.Usuario;
import com.thomazcm.plantae.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class TokenService {
    
    @Autowired
    private UsuarioRepository repository;
    
    @Value("${rest.jwt.expiration}")
    private String expiration;
    
    @Value("${rest.jwt.secret}")
    private String secret;

    public String gerarToken(Authentication authentication) {
        Usuario logado = (Usuario) authentication.getPrincipal();
        Date hoje = new Date();
        Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration)* 60 * 60 * 1000);
        
        return Jwts.builder()
                .setIssuer("plantae")
                .setSubject(logado.getId().toString())
                .setIssuedAt(hoje)
                .setExpiration(dataExpiracao)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
    

    public String refreshToken(String token) {
        Usuario logado = usuarioFromToken(token, repository);
        Date hoje = new Date();
        Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration)* 60 * 60 * 1000);
        return Jwts.builder()
                .setIssuer("plantae")
                .setSubject(logado.getId().toString())
                .setIssuedAt(hoje)
                .setExpiration(dataExpiracao)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    
    public boolean isTokenExpiring(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        Date now = new Date();
        long differenceInMilliseconds = expirationDate.getTime() - now.getTime();
        long hoursLeft = TimeUnit.MILLISECONDS.toHours(differenceInMilliseconds);
        return hoursLeft < 24;
    }
    
    private Date getExpirationDateFromToken(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody();
        return claims.getExpiration();
    }

    public boolean ehValido(String tokenRaw) {
        String token = checkToken(tokenRaw);
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
                | SignatureException | IllegalArgumentException e) {
            return false;
        }
    }
    
    public Usuario usuarioFromToken(String token, UsuarioRepository repo) {
        try {
            return repo.findById(idFromToken(token)).get();
        } catch (NoSuchElementException e) {
            throw new RuntimeException("usuario inexistente");
        }
    }

    public String idFromToken(String tokenRaw) {
        String token = checkToken(tokenRaw);
        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    private String checkToken(String tokenRaw) {
        if (ObjectUtils.isEmpty(tokenRaw)) {
            return null;
        }
        return tokenRaw;
    }

    

}
