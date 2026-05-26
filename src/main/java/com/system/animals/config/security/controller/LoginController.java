package com.system.animals.config.security.controller;

import java.util.HashMap;
import java.util.Map;

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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ValidationResult validation;

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
        response.put("message: ", "Inicio de sesion exitoso...");
        return ResponseEntity.ok(response);
    }
}
