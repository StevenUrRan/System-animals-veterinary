package com.system.animals.exception.valid;

import com.system.animals.modules.user.service.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserNitExistValidation implements ConstraintValidator<UserNitExist, Long> {

    private final UserService userService;

    @Override
    public boolean isValid(Long nit, ConstraintValidatorContext context) {

        return nit == null || !userService.existNit(nit);

    }

}
