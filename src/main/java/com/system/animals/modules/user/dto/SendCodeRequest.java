package com.system.animals.modules.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SendCodeRequest(

    @NotBlank(message = "{user.email.not-blank}")
    @Size(min = 8, max = 50, message = "{user.email.size}")
    @Email(message = "{user.email.valid}")
    @Pattern(
        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
        message = "{user.email.pattern}"
    )
    String email
) {

}
