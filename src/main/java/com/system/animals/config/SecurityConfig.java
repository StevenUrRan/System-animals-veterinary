package com.system.animals.config;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.animals.config.security.filter.JwtAuthorizationFilter;
import com.system.animals.config.security.service.CustomUserDetaislService;
import com.system.animals.config.security.service.JwtService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetaislService userDetaislService;
    private final JwtService jwtService;
    private final MessageSource messageSource;
    private final ObjectMapper objectMapper;

    private static final String[] PUBLIC_ENDPOINTS = {
            "/auth/register",
            "/auth/send-code",
            "/auth/verify",
            "/login",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetaislService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((auth) -> auth

                        .requestMatchers("/auth/register",
                                "/auth/send-code",
                                "/auth/verify",
                                "/login",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html")
                        .permitAll()

                        .requestMatchers("/roles/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET,
                                "/user/list", "/user/email", "/user/nit")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST,
                                "/user/admin", "/user/user-type")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/user/nit")
                        .hasAnyRole("ADMIN", "VETERINARIAN", "KEEPER", "MANAGER", "AUDITOR", "USER")

                        .requestMatchers(HttpMethod.DELETE, "/user/nit")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/veterinary/**")
                        .hasAnyRole("ADMIN", "VETERINARIAN")

                        .requestMatchers(HttpMethod.POST, "/veterinary/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/veterinary/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/veterinary/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/animals/**")
                        .hasAnyRole("ADMIN", "VETERINARIAN", "KEEPER", "MANAGER", "AUDITOR", "USER")

                        .requestMatchers(HttpMethod.POST, "/animals/**")
                        .hasAnyRole("ADMIN", "VETERINARIAN", "KEEPER")

                        .requestMatchers(HttpMethod.PUT, "/animals/**")
                        .hasAnyRole("ADMIN", "VETERINARIAN", "KEEPER")

                        .requestMatchers(HttpMethod.DELETE, "/animals/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/citation/**")
                        .hasAnyRole("ADMIN", "VETERINARIAN")

                        .requestMatchers(HttpMethod.POST, "/citation/**")
                        .hasAnyRole("ADMIN", "VETERINARIAN")

                        .requestMatchers(HttpMethod.PUT, "/citation/**")
                        .hasAnyRole("ADMIN", "VETERINARIAN")

                        .requestMatchers(HttpMethod.DELETE, "/citation/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/history/**")
                        .hasAnyRole("ADMIN", "VETERINARIAN", "AUDITOR")

                        .requestMatchers(HttpMethod.POST, "/history/**")
                        .hasAnyRole("ADMIN", "VETERINARIAN")

                        .requestMatchers(HttpMethod.PUT, "/history/**")
                        .hasAnyRole("ADMIN", "VETERINARIAN")

                        .requestMatchers(HttpMethod.DELETE, "/history/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/Invoice/**")
                        .hasAnyRole("ADMIN", "MANAGER")

                        .requestMatchers(HttpMethod.POST, "/Invoice/**")
                        .hasAnyRole("ADMIN", "MANAGER")

                        .requestMatchers(HttpMethod.PUT, "/Invoice/**")
                        .hasAnyRole("ADMIN", "MANAGER")

                        .requestMatchers(HttpMethod.DELETE, "/Invoice/**")
                        .hasRole("ADMIN")
                        .anyRequest().authenticated())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            Locale locale = LocaleContextHolder.getLocale();
                            String message = messageSource.getMessage("exception.authentication", null, locale);
                            response.getWriter().write(objectMapper.writeValueAsString(Map.of("Message", message)));
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            Locale locale = LocaleContextHolder.getLocale();
                            String message = messageSource.getMessage("exception.access-denied", null, locale);
                            response.getWriter().write(objectMapper.writeValueAsString(Map.of("Message", message)));
                        }))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(new JwtAuthorizationFilter(userDetaislService, jwtService),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization", "X-Total-Count"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(36000L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
