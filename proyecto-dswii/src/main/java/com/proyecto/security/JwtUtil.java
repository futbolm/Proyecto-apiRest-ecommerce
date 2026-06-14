package com.proyecto.security;
 
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
 
@Component
public class JwtUtil {
 
    private static final String SECRET_STRING = "mi_clave_super_secreta_de_256_bytes!!";
    private SecretKey key;
 
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));
    }
 
    // Token simple (compatible con el ejemplo del profe)
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
 
    // Token con ROL incluido (para que SecurityConfig pueda verificar permisos)
    public String generateTokenConRol(String email, String rol) {
        return Jwts.builder()
                .setSubject(email)
                .claim("rol", rol)          // ← guardamos el rol aquí
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24h
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
 
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
    }
 
    public boolean validateToken(String token, String email) {
        try {
            return getClaims(token).getSubject().equals(email);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}