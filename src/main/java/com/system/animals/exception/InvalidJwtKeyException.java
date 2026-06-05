package com.system.animals.exception;

/**
 * Excepción lanzada cuando la clave JWT tiene una longitud inválida
 */
public class InvalidJwtKeyException extends RuntimeException {

    public InvalidJwtKeyException() {
        super("exception.jwt.key.invalid");
    }
}
