package com.system.animals.modules.user.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.system.animals.exception.PageableNotFountException;
import com.system.animals.exception.RoleExistException;
import com.system.animals.exception.RoleNotFoundException;
import com.system.animals.modules.user.dto.RoleDto;
import com.system.animals.modules.user.entity.Role;
import com.system.animals.modules.user.mapper.RoleMapper;
import com.system.animals.modules.user.repository.RoleRepository;
import com.system.animals.modules.user.service.RoleService;
import com.system.animals.shared.enums.TypeRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public Page<RoleDto> findAll(Pageable pageable) {
        if (pageable == null || !pageable.isPaged()) {
            throw new PageableNotFountException();
        }

        Page<Role> roleEntity = roleRepository.findAll(pageable);
        return roleEntity.map(role -> new RoleDto(
                role.getName()));
    }

    @Override
    public RoleDto newRole(RoleDto roleDto) {

        if (roleRepository.existsByName(roleDto.name())) {
            throw new RoleExistException();
        }

        Role role = roleMapper.toEntity(roleDto);

        Role save = roleRepository.save(role);

        return roleMapper.toDto(save);

    }

    @Override
    public RoleDto updateRole(RoleDto roleDto, TypeRole name) {
        Role role = roleRepository.findByName(name).orElseThrow(RoleNotFoundException::new);

        role.setName(roleDto.name());
        Role save = roleRepository.save(role);
        return roleMapper.toDto(save);

    }

    @Override
    public void deleteRole(TypeRole name) {
        Role role = roleRepository.findByName(name).orElseThrow(RoleNotFoundException::new);

        if (!role.isEnable()) {
            throw new RoleNotFoundException();
        }
        role.setEnable(false);
        roleRepository.save(role);
    }

}
