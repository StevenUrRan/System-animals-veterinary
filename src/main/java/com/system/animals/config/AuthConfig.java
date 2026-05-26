package com.system.animals.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing
public class AuthConfig {

    @Bean
    public AuditorAware<String> auditProvider(){

        return() -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())){
                return Optional.of("System");
            }
            return Optional.of(authentication.getName());
        };
    }

}
