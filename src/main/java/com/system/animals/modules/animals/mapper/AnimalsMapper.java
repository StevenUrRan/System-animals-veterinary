package com.system.animals.modules.animals.mapper;

import org.mapstruct.Mapper;

import com.system.animals.modules.animals.dto.AnimalsDto;
import com.system.animals.modules.animals.entity.Animals;

@Mapper(componentModel = "spring")
public interface AnimalsMapper {

    Animals toEntity(AnimalsDto animalsDto);

    AnimalsDto toDto(Animals animals);

}
