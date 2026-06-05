package com.system.animals.exception;

/**
 * Excepción lanzada cuando hay un problema validando el token JWT
 */
public class TokenValidationException extends RuntimeException {

    public TokenValidationException() {
        super("exception.jwt.token.invalid");
    }
}
