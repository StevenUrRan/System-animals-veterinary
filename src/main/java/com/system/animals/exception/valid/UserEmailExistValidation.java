package com.system.animals.exception.valid;

import com.system.animals.modules.user.service.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserEmailExistValidation implements ConstraintValidator<UserEmailExist, String> {

    private final UserService userService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email == null || email.isBlank() || !userService.existEmail(email);
    }

}
