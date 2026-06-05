package com.system.animals.exception;

/**
 * Excepción lanzada cuando el rango para generar un código es inválido
 */
public class CodeRangeInvalidException extends RuntimeException {

    public CodeRangeInvalidException() {
        super("exception.code.range-invalid");
    }
}
