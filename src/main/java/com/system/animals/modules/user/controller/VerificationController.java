package com.system.animals.modules.user.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.system.animals.modules.user.dto.RegisterRequest;
import com.system.animals.modules.user.dto.SendCodeRequest;
import com.system.animals.modules.user.dto.VerifyCodeRequest;
import com.system.animals.modules.user.service.VerifyCodeService;
import com.system.animals.shared.enums.VerifyCodeResult;
import com.system.animals.shared.valid.ValidationResult;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class VerificationController {

        private final VerifyCodeService verificationService;
        private final ValidationResult validationResult;

        @PostMapping("/register")
        public ResponseEntity<?> register(
                        @Valid @RequestBody RegisterRequest request,
                        BindingResult result) {
                if (result.hasFieldErrors()) {
                        return validationResult.validation(result);
                }

                verificationService.register(request);

                return ResponseEntity.status(HttpStatus.CREATED).body(
                                response("Usuario registrado. Revisa tu correo para verificar la cuenta"));
        }

        @PostMapping("/send-code")
        public ResponseEntity<?> sendCode(
                        @Valid @RequestBody SendCodeRequest request,
                        BindingResult result) {
                if (result.hasFieldErrors()) {
                        return validationResult.validation(result);
                }

                verificationService.sendCode(request.email());

                return ResponseEntity.ok(response("Codigo enviado"));
        }

        @PostMapping("/verify")
        public ResponseEntity<?> verify(
                        @Valid @RequestBody VerifyCodeRequest request,
                        BindingResult result) {
                if (result.hasFieldErrors()) {
                        return validationResult.validation(result);
                }

                VerifyCodeResult verifyCodeResult = verificationService.verifyCode(
                                request.email(),
                                request.code());

                return switch (verifyCodeResult) {
                        case VERIFIED -> ResponseEntity.ok(response(verifyCodeResult.getMessage()));
                        case CODE_NOT_FOUND, USER_NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                                        .body(response(verifyCodeResult.getMessage()));
                        case CODE_EXPIRED, CODE_INVALID -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                        .body(response(verifyCodeResult.getMessage()));
                };
        }

        private Map<String, String> response(String message) {
                return Map.of("message", message);
        }
}
