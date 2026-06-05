package com.system.animals.shared.enums;

public enum VerifyCodeResult {

    VERIFIED("controller.verification.verify.success"),
    CODE_NOT_FOUND("controller.verification.verify.not-found"),
    CODE_EXPIRED("controller.verification.verify.expired"),
    CODE_INVALID("controller.verification.verify.invalid"),
    USER_NOT_FOUND("controller.verification.verify.not-found");

    private final String messageKey;

    VerifyCodeResult(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }

    @Deprecated(forRemoval = true)
    public String getMessage() {
        return messageKey;
    }
}
