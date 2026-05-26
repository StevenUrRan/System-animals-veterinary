package com.system.animals.exception.valid;

import com.system.animals.modules.animals.service.AnimalsService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AnimalsNitExistValidation implements ConstraintValidator<AnimalsNitExist, Long> {

    private final AnimalsService animalsService;

    @Override
    public boolean isValid(Long nit, ConstraintValidatorContext context) {
        return animalsService.existByNit(nit);
    }

}
