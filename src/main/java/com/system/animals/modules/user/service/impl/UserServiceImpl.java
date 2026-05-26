package com.system.animals.modules.user.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.system.animals.exception.EmailExistException;
import com.system.animals.exception.EmailNotFoundException;
import com.system.animals.exception.NitNotFoundException;
import com.system.animals.exception.PageableNotFountException;
import com.system.animals.exception.RoleNotFoundException;
import com.system.animals.exception.UserNotEnableException;
import com.system.animals.modules.user.dto.UserRequestDto;
import com.system.animals.modules.user.dto.UserResponseDto;
import com.system.animals.modules.user.dto.UserUpdateDto;
import com.system.animals.modules.user.entity.Role;
import com.system.animals.modules.user.entity.User;
import com.system.animals.modules.user.mapper.UserMapper;
import com.system.animals.modules.user.repository.RoleRepository;
import com.system.animals.modules.user.repository.UserRepository;
import com.system.animals.modules.user.service.UserService;
import com.system.animals.shared.enums.TypeRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> findAll(Pageable pageable) {
        if (pageable == null || !pageable.isPaged()) {
            throw new PageableNotFountException();
        }
        Page<User> toUserEntity = userRepository.findAll(pageable);
        return toUserEntity.map(user -> new UserResponseDto(
                user.getUsernam(),
                user.getEmail(),
                user.getNit(),
                user.getDocument(),
                user.getGender()));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto findByNit(Long nit) {

        User userNit = userRepository.findByNit(nit)
                .orElseThrow(NitNotFoundException::new);
        return userMapper.toDto(userNit);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto findByEmail(String email) {

        User userEmail = userRepository.findByEmail(email)
                .orElseThrow(EmailNotFoundException::new);
        return userMapper.toDto(userEmail);
    }

    @Override
    @Transactional
    public UserResponseDto newAdmin(UserRequestDto requestDto) {

        Set<Role> roles = new HashSet<>();

        roles.add(roleRepository.findByName(TypeRole.ROLE_ADMIN)
                .orElseThrow(RoleNotFoundException::new));
        roles.add(roleRepository.findByName(TypeRole.ROLE_AUDITOR)
                .orElseThrow(RoleNotFoundException::new));
        roles.add(roleRepository.findByName(TypeRole.ROLE_KEEPER)
                .orElseThrow(RoleNotFoundException::new));
        roles.add(roleRepository.findByName(TypeRole.ROLE_MANAGER)
                .orElseThrow(RoleNotFoundException::new));
        roles.add(roleRepository.findByName(TypeRole.ROLE_USER)
                .orElseThrow(RoleNotFoundException::new));
        roles.add(roleRepository.findByName(TypeRole.ROLE_VETERINARIAN)
                .orElseThrow(RoleNotFoundException::new));

        User user = userMapper.toEntity(requestDto);
        user.setRole(roles);
        user.setEnable(true);
        user.setPassword(passwordEncoder.encode(requestDto.password()));

        User save = userRepository.save(user);

        return userMapper.toDto(save);

    }

    @Override
    @Transactional
    public UserResponseDto newUserType(UserRequestDto requestDto) {

        User user = userMapper.toEntity(requestDto);
        if (requestDto.typeRole() == null) {
            throw new RoleNotFoundException();
        }
        Role role = roleRepository.findByName(requestDto.typeRole()).orElseThrow(RoleNotFoundException::new);
        if (user.getRole() == null) {
            user.setRole(new HashSet<>());
        }
        user.getRole().add(role);
        user.setEnable(true);
        user.setPassword(passwordEncoder.encode(requestDto.password()));

        User save = userRepository.save(user);
        return userMapper.toDto(save);
    }

    @Override
    @Transactional
    public UserResponseDto update(UserUpdateDto updateDto, Long nit) {

        User user = userRepository.findByNit(nit).orElseThrow(NitNotFoundException::new);

        if (!user.getEmail().equals(updateDto.email()) && userRepository.existsByEmail(updateDto.email())) {
            throw new EmailExistException();
        }

        user.setEmail(updateDto.email());
        user.setPassword(passwordEncoder.encode(updateDto.password()));
        user.setUsernam(updateDto.usernam());

        User save = userRepository.save(user);
        return userMapper.toDto(save);

    }

    @Override
    @Transactional
    public void deleteUser(Long nit) {
        User user = userRepository.findByNit(nit)
                .orElseThrow(NitNotFoundException::new);

        if (!user.isEnable()) {
            throw new UserNotEnableException();
        }
        user.setEnable(false);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existNit(Long nit) {
        return userRepository.existsByNit(nit);
    }
}
