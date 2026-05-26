package com.system.animals.modules.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.system.animals.modules.user.dto.RoleDto;
import com.system.animals.shared.enums.TypeRole;

public interface RoleService {

    Page<RoleDto> findAll(Pageable pageable);

    RoleDto newRole(RoleDto roleDto);

    RoleDto updateRole(RoleDto roleDto, TypeRole name);

    void deleteRole(TypeRole name);

}
