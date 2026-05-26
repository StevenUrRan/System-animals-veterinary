package com.system.animals.config.security.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String secreKey;

    @Value("${jwt.expiration}")
    private long timeExpiration;

    private SecretKey getSecretKey() {

        if (secreKey == null || secreKey.isBlank()) {
            throw new RuntimeException("La llave no fue cargada");
        }
        byte[] keyBytes = secreKey.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new RuntimeException("La llave tiene que tener como minimo 32 bist");
        }
        return Keys.hmacShaKeyFor(keyBytes);

    }

    public String generateToken(UserDetails userDetails) {
        log.info("Generando el Token");

        String token = Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + timeExpiration))
                .signWith(getSecretKey())
                .compact();

        log.info("token generado exisotamene");
        return token;
    }

    private Claims extractClaims(String token) {
        try {
            log.debug("Validando la firma del token");
            Claims claims = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            log.debug("firma del tooken validad");

            return claims;
        } catch (Exception e) {
            log.error("Token no valido");
            throw new RuntimeException("Token invalido o expirado");
        }
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    private boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isValid(String token, UserDetails userDetails) {
        try {
            log.info("🔎 Validando token para usuario: {}", userDetails.getUsername());
            String username = extractUsername(token);
            boolean isExpired = isTokenExpired(token);
            boolean usernameMatches = username.equals(userDetails.getUsername());

            log.info("   - Usuario en token: {}", username);
            log.info("   - Usuario esperado: {}", userDetails.getUsername());
            log.info("   - ¿Usuario coincide?: {}", usernameMatches);
            log.info("   - ¿Token expirado?: {}", isExpired);

            boolean isTokenValid = usernameMatches && !isExpired;
            log.info("   ➜ Resultado final: {} {}", isTokenValid ? "✅ VÁLIDO" : "❌ INVÁLIDO",
                    isTokenValid ? "- Token autenticado correctamente" : "");

            return isTokenValid;
        } catch (Exception e) {
            log.error("❌ Error en validación de token: {}", e.getMessage());
            return false;
        }
    }
}
