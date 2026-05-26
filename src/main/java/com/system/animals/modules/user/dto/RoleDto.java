package com.system.animals.modules.user.dto;

import com.system.animals.shared.enums.TypeRole;

import jakarta.validation.constraints.NotNull;

public record RoleDto(

    @NotNull(message = "{role.name.not-null}")
    TypeRole name) {

}
