package com.system.animals.modules.user.dto;

import com.system.animals.exception.valid.UserEmailExist;
import com.system.animals.exception.valid.UserNitExist;
import com.system.animals.shared.enums.TypeDocument;
import com.system.animals.shared.enums.UserGender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "{user.username.not-blank}")
        @Size(min = 8, max = 50, message = "{user.username.size}")
        String username,

        @UserEmailExist(message = "{user.email.exists}")
        @NotBlank(message = "{user.email.not-blank}")
        @Size(min = 8, max = 50, message = "{user.email.size}")
        @Email(message = "{user.email.valid}")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "{user.email.pattern}")
        String email,

        @NotBlank(message = "{user.password.not-blank}")
        @Size(min = 8, max = 100, message = "{user.password.size}")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_]).*$", message = "{user.password.pattern}")
        String password,

        @UserNitExist(message = "{user.nit.exists}")
        @NotNull(message = "{user.nit.not-null}")
        @Min(value = 1000000000L, message = "{user.nit.min}")
        @Max(value = 9999999999L, message = "{user.nit.max}")
        Long nit,

        @NotNull(message = "{user.document.not-null}")
        TypeDocument document,
    
        @NotNull(message = "{user.type-gender.not-null}")
        UserGender gender

) {
}
