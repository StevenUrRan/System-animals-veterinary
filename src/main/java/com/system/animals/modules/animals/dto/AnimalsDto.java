package com.system.animals.modules.animals.dto;

import com.system.animals.exception.valid.AnimalsNitExist;
import com.system.animals.modules.user.dto.UserResponseDto;
import com.system.animals.shared.enums.AnimalGender;
import com.system.animals.shared.enums.TypeAnimals;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AnimalsDto(

        @NotBlank(message = "{animal.name.not-blank}")
        @Size(min = 2, max = 50, message = "{animal.name.size}")
        String name,

        @NotNull(message = "{animal.age.not-null}")
        @Min(value = 0, message = "{animal.age.min}")
        @Max(value = 100, message = "{animal.age.max}")
        Integer age,

        @NotNull(message = "{animal.gender.not-null}")
        AnimalGender gender,

        @NotNull(message = "{animal.type.not-null}")
        TypeAnimals type,

        @AnimalsNitExist
        @NotNull(message = "{animal.nit.not-null}")
        @Min(value = 1000000000L, message = "{animal.nit.min}")
        @Max(value = 9999999999L, message = "{animal.nit.max}")
        Long nit,

        @Size(max = 100, message = "{animal.other-type.size}")
        String otherTypeAnimals,

        @NotNull(message = "{animal.user.not-null}")
        UserResponseDto userDto) {


}
