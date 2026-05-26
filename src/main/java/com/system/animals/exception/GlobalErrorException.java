package com.system.animals.exception;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalErrorException {

    private final MessageSource messageSource;

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<?> emailExistException(RuntimeException e) {
        return buildResponse("exception.email.exists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<?> emailNotFoundException(RuntimeException e) {
        return buildResponse("exception.email.not-found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NitNotFoundException.class)
    public ResponseEntity<?> nitNotFoundException(RuntimeException e) {
        return buildResponse("exception.nit.not-found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> userNotFoundException(RuntimeException e) {
        return buildResponse("exception.user.not-found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotEnableException.class)
    public ResponseEntity<?> userNotEnableException(RuntimeException e) {
        return buildResponse("exception.user.not-enable", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotVeterinaryException.class)
    public ResponseEntity<?> userNotVeterinaryException(RuntimeException e) {
        return buildResponse("exception.user.not-veterinary", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PageableNotFountException.class)
    public ResponseEntity<?> pageableNotFountException(RuntimeException e) {
        return buildResponse("exception.pageable.not-found", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<?> roleNotFoundException(RuntimeException e) {
        return buildResponse("exception.role.not-found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoleExistException.class)
    public ResponseEntity<?> roleExistException(RuntimeException e) {
        return buildResponse("exception.role.exists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(VeterinaryNotFoundException.class)
    public ResponseEntity<?> veterinaryNotFoundException(RuntimeException e) {
        return buildResponse("exception.veterinary.not-found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VeterinaryExistException.class)
    public ResponseEntity<?> veterinaryExistException(RuntimeException e) {
        return buildResponse("exception.veterinary.exists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<?> rateLimitExceededException(RuntimeException e) {
        return buildResponse("exception.rate-limit", HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> illegalStateException(RuntimeException e) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("Message", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> authenticationException(AuthenticationException e) {
        return buildResponse("exception.authentication", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> accessDeniedException(AccessDeniedException e) {
        return buildResponse("exception.access-denied", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("Message", getMessage("exception.validation"));
        errors.put("Errors", e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage(),
                        (first, second) -> first)));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintViolationException(ConstraintViolationException e) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("Message", getMessage("exception.validation"));
        errors.put("Errors", e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        violation -> violation.getMessage(),
                        (first, second) -> first)));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        return buildResponse("exception.invalid-json", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return buildResponse("exception.bad-request", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> dataIntegrityViolationException(DataIntegrityViolationException e) {
        return buildResponse("exception.data-integrity", HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ NoHandlerFoundException.class, NoResourceFoundException.class })
    public ResponseEntity<?> notFoundException(Exception e) {
        return buildResponse("exception.not-found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exception(Exception e) {
        return buildResponse("exception.internal-server", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(String messageKey, HttpStatus status) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("Message", getMessage(messageKey));

        return ResponseEntity.status(status).body(errors);
    }

    private String getMessage(String messageKey) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageKey, null, locale);
    }
}
