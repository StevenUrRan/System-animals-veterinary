package com.system.animals.config.security.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.system.animals.config.security.service.JwtService;
import com.system.animals.modules.user.dto.LoginRequestDto;
import com.system.animals.modules.user.dto.LoginResponseDto;
import com.system.animals.shared.valid.ValidationResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/login")
@Tag(name = "Autenticación", description = "Gestión de autenticación de usuarios: inicio de sesión")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ValidationResult validation;

    @Value("${controller.login.success:Login successful. Welcome back!}")
    private String loginSuccessMessage;

    @Value("${controller.login.invalid:Incorrect username or password. Please try again.}")
    private String loginInvalidMessage;

    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y retorna un token JWT para acceso a recursos protegidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${controller.login.success}"),
            @ApiResponse(responseCode = "400", description = "${controller.login.bad-request}"),
            @ApiResponse(responseCode = "401", description = "${controller.login.unauthorized}")
    })
    @PostMapping
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validation.validation(result);
        }

        Authentication authentication = (Authentication) authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        UserDetails user = (UserDetails) authentication
                .getPrincipal();

        String token = jwtService.generateToken(user);

        Map<String, Object> response = new HashMap<>();

        response.put("data: ", new LoginResponseDto(token));
        response.put("message: ", loginSuccessMessage);
        return ResponseEntity.ok(response);
    }
}