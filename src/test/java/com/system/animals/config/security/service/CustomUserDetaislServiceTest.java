package com.system.animals.config.security.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import com.system.animals.modules.user.repository.UserRepository;

class CustomUserDetaislServiceTest {

    @Test
    void loadUserByUsernameReturnsConfiguredAdminWithoutDatabaseLookup() {
        AtomicBoolean repositoryCalled = new AtomicBoolean(false);
        UserRepository userRepository = (UserRepository) Proxy.newProxyInstance(
                UserRepository.class.getClassLoader(),
                new Class<?>[] { UserRepository.class },
                (proxy, method, args) -> {
                    repositoryCalled.set(true);
                    throw new AssertionError("Admin in memory must not query the database");
                });
        CustomUserDetaislService userDetailsService = new CustomUserDetaislService(userRepository);

        ReflectionTestUtils.setField(userDetailsService, "adminEnabled", true);
        ReflectionTestUtils.setField(userDetailsService, "adminEmail", "admin@test.local");
        ReflectionTestUtils.setField(userDetailsService, "adminPasswordHash", "$2a$10$hash");
        ReflectionTestUtils.setField(userDetailsService, "adminRoles", List.of("ROLE_ADMIN", "ROLE_AUDITOR"));

        UserDetails userDetails = userDetailsService.loadUserByUsername("admin@test.local");

        assertThat(userDetails.getUsername()).isEqualTo("admin@test.local");
        assertThat(userDetails.getPassword()).isEqualTo("$2a$10$hash");
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_AUDITOR");
        assertThat(repositoryCalled).isFalse();
    }
}
