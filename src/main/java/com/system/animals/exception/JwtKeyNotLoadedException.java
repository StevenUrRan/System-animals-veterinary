package com.system.animals.exception;

/**
 * Excepción lanzada cuando la clave JWT no se cargó correctamente
 * desde las propiedades de configuración
 */
public class JwtKeyNotLoadedException extends RuntimeException {

    public JwtKeyNotLoadedException() {
        super("exception.jwt.key.not-loaded");
    }
}
