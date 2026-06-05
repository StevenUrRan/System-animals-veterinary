package com.system.animals.exception;

/**
 * Excepción lanzada cuando se intenta verificar un usuario que ya está
 * verificado
 */
public class UserAlreadyVerifiedException extends RuntimeException {

    public UserAlreadyVerifiedException() {
        super("exception.user.already-verified");
    }
}
