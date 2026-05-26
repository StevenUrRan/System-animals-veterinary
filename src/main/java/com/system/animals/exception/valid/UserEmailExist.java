package com.system.animals.exception.valid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = UserEmailExistValidation.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserEmailExist {

    String message() default "{user.email.exists}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
