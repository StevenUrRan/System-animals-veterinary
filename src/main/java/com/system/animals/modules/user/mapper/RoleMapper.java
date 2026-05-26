package com.system.animals.modules.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.system.animals.modules.user.dto.RoleDto;
import com.system.animals.modules.user.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper{

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enable", ignore = true)
    Role toEntity(RoleDto roleDto);

    RoleDto toDto(Role role);

}
