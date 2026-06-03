package com.system.animals.modules.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.system.animals.modules.user.dto.RegisterRequest;
import com.system.animals.modules.user.dto.UserRequestDto;
import com.system.animals.modules.user.dto.UserResponseDto;
import com.system.animals.modules.user.entity.User;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper{


    @Mapping(target = "enable", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(UserRequestDto userDto);

    @Mapping(target = "enable", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(RegisterRequest request);
 
    UserResponseDto toDto(User user);

    @Mapping(target = "nit", ignore = true)
    @Mapping(target = "document", ignore = true)
    @Mapping(target = "enable", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toUpdate(UserRequestDto userDto);

}
