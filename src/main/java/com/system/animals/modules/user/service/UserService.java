package com.system.animals.modules.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.system.animals.modules.user.dto.UserRequestDto;
import com.system.animals.modules.user.dto.UserResponseDto;
import com.system.animals.modules.user.dto.UserUpdateDto;

public interface UserService {

    Page<UserResponseDto> findAll(Pageable pageable);

    UserResponseDto findByNit(Long nit);

    UserResponseDto findByEmail(String email);

    UserResponseDto newAdmin(UserRequestDto requestDto);

    UserResponseDto newUserType(UserRequestDto requestDto);

    UserResponseDto update(UserUpdateDto requestDto, Long nit);

    void deleteUser(Long nit);

    boolean existEmail(String email);

    boolean existNit(Long nit);
}
