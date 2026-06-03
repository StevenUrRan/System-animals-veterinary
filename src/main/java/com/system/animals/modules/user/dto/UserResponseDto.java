package com.system.animals.modules.user.dto;

import com.system.animals.shared.enums.TypeDocument;
import com.system.animals.shared.enums.UserGender;

import lombok.Builder;

@Builder
public record UserResponseDto(

                String username,

                String email,

                Long nit,

                TypeDocument document,

                UserGender gender

) {

}
