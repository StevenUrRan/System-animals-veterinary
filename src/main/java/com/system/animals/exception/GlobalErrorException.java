package com.system.animals.exception;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    @ExceptionHandler(HistoryAnimalsNotFoundException.class)
    public ResponseEntity<?> historyAnimalsNotFoundException(RuntimeException e) {
        return buildResponse("exception.history.not-found", HttpStatus.NOT_FOUND);
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

    @ExceptionHandler(JwtKeyNotLoadedException.class)
    public ResponseEntity<?> jwtKeyNotLoadedException(RuntimeException e) {
        return buildResponse("exception.jwt.key.not-loaded", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidJwtKeyException.class)
    public ResponseEntity<?> invalidJwtKeyException(RuntimeException e) {
        return buildResponse("exception.jwt.key.invalid", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TokenValidationException.class)
    public ResponseEntity<?> tokenValidationException(RuntimeException e) {
        return buildResponse("exception.jwt.token.invalid", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidCodeLengthException.class)
    public ResponseEntity<?> invalidCodeLengthException(RuntimeException e) {
        return buildResponse("exception.code.invalid-length", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CodeRangeInvalidException.class)
    public ResponseEntity<?> codeRangeInvalidException(RuntimeException e) {
        return buildResponse("exception.code.range-invalid", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyVerifiedException.class)
    public ResponseEntity<?> userAlreadyVerifiedException(RuntimeException e) {
        return buildResponse("exception.user.already-verified", HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler(AnimalsNotFoundException.class)
    public ResponseEntity<?> animalsNotFoundException(RuntimeException e) {
        return buildResponse("exception.animals.not-found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AnimalsNitExistException.class)
    public ResponseEntity<?> animalsNitExistException(RuntimeException e) {
        return buildResponse("exception.animals-nit.exists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DayNotFoundException.class)
    public ResponseEntity<?> dayNotFoundException(RuntimeException e) {
        return buildResponse("exception.day.not-found", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SaturdayException.class)
    public ResponseEntity<?> saturdayException(RuntimeException e) {
        return buildResponse("exception.day.saturday", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NonBusinessDayException.class)
    public ResponseEntity<?> nonBusinessDayException(RuntimeException e) {
        return buildResponse("exception.day.non-business", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HistoryAlreadyExistsException.class)
    public ResponseEntity<?> historyAlreadyExistsException(RuntimeException e) {
        return buildResponse("exception.history.already-exists", HttpStatus.CONFLICT);
    }


    @ExceptionHandler(SlotInvalidException.class)
    public ResponseEntity<?> slotInvalidException(RuntimeException e) {
        return buildResponse("exception.slot.invalid", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvoiceNotFoundException.class)
    public ResponseEntity<?> invoiceNotFoundException(RuntimeException e) {
        return buildResponse("exception.invoice.not-found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CitationNotFoundException.class)
    public ResponseEntity<?> citationNotFoundException(RuntimeException e) {
        return buildResponse("exception.citation.not-found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StatusInvoiceInvalidException.class)
    public ResponseEntity<?> statusInvoiceInvalidException(RuntimeException e) {
        return buildResponse("exception.status-invoice.invalid", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValueDuplicateException.class)
    public ResponseEntity<?> valueDuplicateException(RuntimeException e) {
        return buildResponse("exception.value.duplicate", HttpStatus.CONFLICT);
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
