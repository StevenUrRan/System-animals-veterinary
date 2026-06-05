package com.system.animals.config.security.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.system.animals.exception.EmailNotFoundException;
import com.system.animals.modules.user.entity.User;
import com.system.animals.modules.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetaislService implements UserDetailsService {

    private final UserRepository userRepository;

    @Value("${app.admin.enabled:true}")
    private boolean adminEnabled;

    @Value("${app.admin.email:admin@system-animals.local}")
    private String adminEmail;

    @Value("${app.admin.password-hash}")
    private String adminPasswordHash;

    @Value("#{'${app.admin.roles:ROLE_ADMIN}'.split(',')}")
    private List<String> adminRoles;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        if (adminEnabled && adminEmail.equalsIgnoreCase(email)) {
            List<SimpleGrantedAuthority> authorities = adminRoles.stream()
                    .map(String::trim)
                    .filter(role -> !role.isBlank())
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            return org.springframework.security.core.userdetails.User.builder()
                    .username(adminEmail)
                    .password(adminPasswordHash)
                    .authorities(authorities)
                    .build();
        }

        User user = userRepository.findByEmail(email).orElseThrow(EmailNotFoundException::new);
        log.info("usuario encontado: ", email);

        List<SimpleGrantedAuthority> authorities = user.getRole().stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getName().name())).collect(Collectors.toList());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .disabled(!user.isEnable())
                .authorities(authorities)
                .build();
    }

}
