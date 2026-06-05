package com.system.animals.modules.user.controller;

import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@Tag(name = "Verificación", description = "Gestión de verificación de usuarios: registro, envío y verificación de códigos")
@RequiredArgsConstructor
public class VerificationController {

    private final VerifyCodeService verificationService;
    private final ValidationResult validationResult;
    private final MessageSource messageSource;

    @Value("${controller.verification.register.success}")
    private String registerSuccessMessage;

    @Value("${controller.verification.code-sent.success}")
    private String codeSentSuccessMessage;

    @Value("${controller.verification.verify.success}")
    private String verifySuccessMessage;

    @Value("${controller.verification.verify.not-found}")
    private String verifyNotFoundMessage;

    @Value("${controller.verification.verify.expired}")
    private String verifyExpiredMessage;

    @Value("${controller.verification.verify.invalid}")
    private String verifyInvalidMessage;

    @Operation(summary = "Registrar nuevo usuario", description = "Registra un nuevo usuario y envía código de verificación al correo electrónico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "${controller.verification.register.success}"),
            @ApiResponse(responseCode = "400", description = "${controller.verification.register.invalid}")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request,
            BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationResult.validation(result);
        }

        verificationService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                response(registerSuccessMessage));
    }

    @Operation(summary = "Enviar código de verificación", description = "Envía un código de verificación al correo electrónico del usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${controller.verification.code-sent.success}"),
            @ApiResponse(responseCode = "400", description = "${controller.verification.send-code.invalid}"),
            @ApiResponse(responseCode = "429", description = "${controller.verification.send-code.rate-limit}")
    })
    @PostMapping("/send-code")
    public ResponseEntity<?> sendCode(
            @Valid @RequestBody SendCodeRequest request,
            BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationResult.validation(result);
        }

        verificationService.sendCode(request.email());

        return ResponseEntity.ok(response(codeSentSuccessMessage));
    }

    @Operation(summary = "Verificar código de verificación", description = "Verifica el código de verificación enviado al correo electrónico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${controller.verification.verify.success}"),
            @ApiResponse(responseCode = "404", description = "${controller.verification.verify.not-found}"),
            @ApiResponse(responseCode = "400", description = "${controller.verification.verify.expired} o ${controller.verification.verify.invalid}"),
            @ApiResponse(responseCode = "429", description = "${controller.verification.verify.rate-limit}")
    })
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

        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(verifyCodeResult.getMessageKey(), null, locale);

        return switch (verifyCodeResult) {
            case VERIFIED -> ResponseEntity.ok(response(message));
            case CODE_NOT_FOUND, USER_NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(response(message));
            case CODE_EXPIRED, CODE_INVALID -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response(message));
        };
    }

    private Map<String, String> response(String message) {
        return Map.of("message", message);
    }
}