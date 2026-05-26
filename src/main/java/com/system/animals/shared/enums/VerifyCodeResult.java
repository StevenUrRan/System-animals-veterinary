package com.system.animals.shared.enums;

public enum VerifyCodeResult {

    VERIFIED("Usuario verificado correctamente"),
    CODE_NOT_FOUND("Codigo no encontrado"),
    CODE_EXPIRED("Codigo expirado"),
    CODE_INVALID("Codigo incorrecto"),
    USER_NOT_FOUND("Usuario no encontrado");

    private final String message;

    VerifyCodeResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
