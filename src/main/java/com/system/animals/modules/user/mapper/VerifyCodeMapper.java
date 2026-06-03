package com.system.animals.modules.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.system.animals.modules.user.dto.SendCodeRequest;
import com.system.animals.modules.user.entity.VerifyCode;

@Mapper(componentModel = "spring")
public interface VerifyCodeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "expirationTime", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "enable", ignore = true)
    VerifyCode toEntity(SendCodeRequest request);

    SendCodeRequest toDto(VerifyCode code);

}