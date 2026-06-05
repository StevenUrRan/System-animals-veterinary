package com.system.animals.config;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Autowired
    private MessageSource messageSource;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("System Animals Veterinary API")
                        .version("1.0")
                        .description("API para la gestión de un sistema veterinario"))
                .schemaRequirement("bearerAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Token JWT de autenticación"));
    }

    @Bean
    public OpenApiCustomizer openApiMessageResolver() {
        return openApi -> {
            String description = openApi.getInfo().getDescription();
            if (description != null) {
                openApi.getInfo().setDescription(resolveMessages(description));
            }

            openApi.getPaths().forEach((path, pathItem) -> {
                pathItem.readOperations().forEach(operation -> {

                    String opDescription = operation.getDescription();
                    if (opDescription != null) {
                        operation.setDescription(resolveMessages(opDescription));
                    }

                    String opSummary = operation.getSummary();
                    if (opSummary != null) {
                        operation.setSummary(resolveMessages(opSummary));
                    }

                    if (operation.getResponses() != null) {
                        operation.getResponses().forEach((code, response) -> {
                            String responseDesc = response.getDescription();
                            if (responseDesc != null) {
                                response.setDescription(resolveMessages(responseDesc));
                            }
                        });
                    }
                });
            });
        };
    }

    private String resolveMessages(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        Pattern pattern = Pattern.compile("#\\{([^}]+)}");
        Matcher matcher = pattern.matcher(text);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String key = matcher.group(1);
            try {
                String resolved = messageSource.getMessage(key, null, key, Locale.getDefault());
                matcher.appendReplacement(sb, Matcher.quoteReplacement(resolved));
            } catch (Exception e) {
                matcher.appendReplacement(sb, Matcher.quoteReplacement(key));
            }
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}