package com.system.animals.modules.veterinary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.system.animals.modules.user.mapper.UserMapper;
import com.system.animals.modules.veterinary.dto.VeterinaryDto;
import com.system.animals.modules.veterinary.entity.Veterinary;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface VeterinaryMapper {

    @Mapping(target = "enable", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "license", ignore = true)
    Veterinary toEntity(VeterinaryDto veterinaryDto);

    @Mapping(target = "userDto", source = "user")
    VeterinaryDto toDto(Veterinary veterinary);

    @Mapping(target = "enable", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "license", ignore = true)
    void updateEntityFromDto(VeterinaryDto veterinaryDto, @MappingTarget Veterinary veterinary);
}
