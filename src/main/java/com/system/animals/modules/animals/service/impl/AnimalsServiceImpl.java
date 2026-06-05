package com.system.animals.modules.animals.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.system.animals.exception.AnimalsNotFoundException;
import com.system.animals.exception.PageableNotFountException;
import com.system.animals.exception.UserNotFoundException;
import com.system.animals.modules.animals.dto.AnimalsDto;
import com.system.animals.modules.animals.entity.Animals;
import com.system.animals.modules.animals.mapper.AnimalsMapper;
import com.system.animals.modules.animals.repository.AnimalsRepository;
import com.system.animals.modules.animals.service.AnimalsService;
import com.system.animals.modules.user.entity.User;
import com.system.animals.modules.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimalsServiceImpl implements AnimalsService {

    private final AnimalsRepository animalsRepository;
    private final AnimalsMapper animalsMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<AnimalsDto> findAll(Pageable pageable) {
        if (pageable == null || !pageable.isPaged()) {
            throw new PageableNotFountException();
        }
        Page<Animals> toEntityAnimals = animalsRepository.findAllByEnableTrue(pageable);
        return toEntityAnimals.map(animalsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public AnimalsDto findByNit(Long nit) {
        Animals animals = animalsRepository.findByNitAndEnableTrue(nit).orElseThrow(AnimalsNotFoundException::new);
        return animalsMapper.toDto(animals);
    }

    @Override
    @Transactional
    public AnimalsDto newAnimals(AnimalsDto animalsDto) {
        Animals animals = animalsMapper.toEntity(animalsDto);
        animals.setEnable(true);

        animals.setUser(resolveUserByNit(animalsDto));
        Animals save = animalsRepository.save(animals);
        return animalsMapper.toDto(save);
    }

    @Override
    @Transactional
    public AnimalsDto updateAnimals(Long nit, AnimalsDto animalsDto) {
        Animals animals = animalsRepository.findByNitAndEnableTrue(nit).orElseThrow(AnimalsNotFoundException::new);

        animalsMapper.updateEntityFromDto(animalsDto, animals);

        animals.setUser(resolveUserByNit(animalsDto));

        Animals save = animalsRepository.save(animals);
        return animalsMapper.toDto(save);
    }

    @Override
    @Transactional
    public void delete(Long nit) {
        Animals animals = animalsRepository.findByNitAndEnableTrue(nit)
                .orElseThrow(AnimalsNotFoundException::new);
        animals.setEnable(false);
        animalsRepository.save(animals);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existByNit(Long nit) {
        if (nit == null) {
            return false;
        }
        return !animalsRepository.existsByNit(nit);
    }

    private User resolveUserByNit(AnimalsDto animalsDto) {
        if (animalsDto.userDto() == null || animalsDto.userDto().nit() == null) {
            throw new UserNotFoundException();
        }

        return userRepository.findByNit(animalsDto.userDto().nit())
                .orElseThrow(UserNotFoundException::new);
     }

}
