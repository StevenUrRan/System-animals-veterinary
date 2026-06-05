package com.system.animals.exception;

/**
 * Excepción lanzada cuando se intenta generar un código con longitud inválida
 */
public class InvalidCodeLengthException extends RuntimeException {

    public InvalidCodeLengthException() {
        super("exception.code.invalid-length");
    }
}
