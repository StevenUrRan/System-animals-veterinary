package com.system.animals.modules.veterinary.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.system.animals.exception.PageableNotFountException;
import com.system.animals.exception.UserNotEnableException;
import com.system.animals.exception.UserNotFoundException;
import com.system.animals.exception.UserNotVeterinaryException;
import com.system.animals.exception.VeterinaryExistException;
import com.system.animals.exception.VeterinaryNotFoundException;
import com.system.animals.modules.user.entity.Role;
import com.system.animals.modules.user.entity.User;
import com.system.animals.modules.user.repository.UserRepository;
import com.system.animals.modules.veterinary.dto.VeterinaryDto;
import com.system.animals.modules.veterinary.entity.Veterinary;
import com.system.animals.modules.veterinary.mapper.VeterinaryMapper;
import com.system.animals.modules.veterinary.repository.VeterinaryRepository;
import com.system.animals.modules.veterinary.service.VeterinaryService;
import com.system.animals.shared.enums.TypeRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VeterinaryServiceImpl implements VeterinaryService {

    private final VeterinaryRepository veterinaryRepository;
    private final UserRepository userRepository;
    private final VeterinaryMapper veterinaryMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<VeterinaryDto> findAll(Pageable pageable) {
        if (pageable == null || !pageable.isPaged()) {
            throw new PageableNotFountException();
        }
        Page<Veterinary> toEntity = veterinaryRepository.findAllByEnableTrue(pageable);
        return toEntity.map(veterinaryMapper::toDto);

    }

    @Override
    @Transactional(readOnly = true)
    public VeterinaryDto findByNit(Long license) {

        Veterinary veterinary = veterinaryRepository.findByLicenseAndEnableTrue(license)
                .orElseThrow(VeterinaryNotFoundException::new);
        return veterinaryMapper.toDto(veterinary);

    }

    @Override
    @Transactional
    public VeterinaryDto newVeterinary(Long nit, VeterinaryDto veterinaryDto) {
        User user = validateVeterinaryUser(nit);
        if (veterinaryRepository.existsByUserId(user.getId())) {
            throw new VeterinaryExistException();
        }

        Veterinary veterinary = veterinaryMapper.toEntity(veterinaryDto);
        veterinary.setUser(user);
        veterinary.setLicense(user.getNit());
        veterinary.setEnable(true);

        Veterinary save = veterinaryRepository.save(veterinary);
        return veterinaryMapper.toDto(save);

    }

    @Override
    @Transactional
    public VeterinaryDto upodateVeterinary(Long nit, VeterinaryDto veterinaryDto) {
        User user = validateVeterinaryUser(nit);
        Veterinary veterinary = veterinaryRepository.findByLicenseAndEnableTrue(user.getNit())
                .orElseThrow(VeterinaryNotFoundException::new);
        veterinaryMapper.updateEntityFromDto(veterinaryDto, veterinary);
        veterinary.setUser(user);
        veterinary.setLicense(user.getNit());

        Veterinary save = veterinaryRepository.save(veterinary);
        return veterinaryMapper.toDto(save);
    }

    @Override
    @Transactional
    public VeterinaryDto deleteVeterinary(Long license) {
        Veterinary veterinary = veterinaryRepository.findByLicenseAndEnableTrue(license)
                .orElseThrow(VeterinaryNotFoundException::new);
        veterinary.setEnable(false);
        veterinaryRepository.save(veterinary);
        return veterinaryMapper.toDto(veterinary);
    }

    private User validateVeterinaryUser(Long nit) {
        User user = userRepository.findByNit(nit).orElseThrow(UserNotFoundException::new);
        if (!user.isEnable()) {
            throw new UserNotEnableException();
        }

        boolean isVeterinary = user.getRole().stream()
                .map(Role::getName)
                .anyMatch(role -> role == TypeRole.ROLE_VETERINARIAN);
        if (!isVeterinary) {
            throw new UserNotVeterinaryException();
        }
        return user;
    }

}
