package com.system.animals.modules.veterinary.dto;

import java.math.BigDecimal;

import com.system.animals.modules.user.dto.UserResponseDto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VeterinaryDto(

        @NotNull(message = "{veterinary.age.not-null}")
        @Min(value = 18, message = "{veterinary.age.min}")
        @Max(value = 100, message = "{veterinary.age.max}")
        Integer age,

        @NotNull(message = "{veterinary.years-of-experience.not-null}")
        @Min(value = 0, message = "{veterinary.years-of-experience.min}")
        @Max(value = 80, message = "{veterinary.years-of-experience.max}")
        Integer yearsOfExperience,

        @NotNull(message = "{veterinary.salary.not-null}")
        @DecimalMin(value = "0.0", inclusive = false, message = "{veterinary.salary.min}")
        @Digits(integer = 12, fraction = 2, message = "{veterinary.salary.digits}")
        BigDecimal salary,

        @NotBlank(message = "{veterinary.phone.not-blank}")
        @Size(min = 7, max = 20, message = "{veterinary.phone.size}")
        @Pattern(regexp = "^[0-9+()\\-\\s]+$", message = "{veterinary.phone.pattern}")
        String phone,

        @NotNull(message = "{veterinary.user.not-null}")
        UserResponseDto userDto) {
}
