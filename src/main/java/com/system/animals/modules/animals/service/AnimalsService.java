package com.system.animals.modules.animals.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.system.animals.modules.animals.dto.AnimalsDto;

public interface AnimalsService {

    Page<AnimalsDto> findAll(Pageable pageable);

    AnimalsDto findByNit(Long nit);

    AnimalsDto newAnimals(AnimalsDto animalsDto);

    AnimalsDto updateAnimals(Long nit, AnimalsDto animalsDto);

    void delete(Long nit);

    boolean existByNit(Long nit);
}
