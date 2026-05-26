package com.system.animals.config.security.filter;

import static com.system.animals.config.security.filter.StaticVaribleFilter.AUTHORIZATION;
import static com.system.animals.config.security.filter.StaticVaribleFilter.BEARER;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.system.animals.config.security.service.CustomUserDetaislService;
import com.system.animals.config.security.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final CustomUserDetaislService userDetaislService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("JWT Authentication Filter - Validando request");
        log.info("Endpoint: {} {}", request.getMethod(), request.getRequestURI());

        String authHeader = request.getHeader(AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(BEARER)) {

            log.warn("No Authorization header o formato inválido (Bearer token requerido)");
            log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

            filterChain.doFilter(request, response);
            return;
        }

        log.info("Authorization header encontrado");
        String token = authHeader.substring(7);

        try {
            String email = jwtService.extractUsername(token);
            log.info("Email del token: {}", email);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                log.info("Cargando detalles del usuario...");
                UserDetails userDetails = userDetaislService.loadUserByUsername(email);

                if (jwtService.isValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    userDetails.getAuthorities();
                } else {
                    log.warn("Token inválido para el usuario {}", email);
                }
            }
        } catch (Exception e) {
            log.error("Error durante validación de token: {}", e.getMessage());
        }

        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        filterChain.doFilter(request, response);
    }

}
