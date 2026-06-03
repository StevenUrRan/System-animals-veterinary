package com.system.animals.modules.animals.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.system.animals.modules.animals.dto.AnimalsDto;
import com.system.animals.modules.animals.entity.Animals;
import com.system.animals.modules.user.mapper.UserMapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface AnimalsMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "enable", ignore = true)
    Animals toEntity(AnimalsDto animalsDto);

    @Mapping(target = "userDto", source = "user")
    AnimalsDto toDto(Animals animals);

    @Mapping(target = "nit", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "enable", ignore = true)
    void updateEntityFromDto(AnimalsDto animalsDto, @MappingTarget Animals animals);

}
