package com.system.animals.modules.user.service.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.system.animals.exception.EmailNotFoundException;
import com.system.animals.exception.RateLimitExceededException;
import com.system.animals.exception.RoleNotFoundException;
import com.system.animals.modules.user.dto.RegisterRequest;
import com.system.animals.modules.user.entity.Role;
import com.system.animals.modules.user.entity.User;
import com.system.animals.modules.user.entity.VerifyCode;
import com.system.animals.modules.user.mapper.UserMapper;
import com.system.animals.modules.user.repository.RoleRepository;
import com.system.animals.modules.user.repository.UserRepository;
import com.system.animals.modules.user.repository.VerifyCodeRepository;
import com.system.animals.modules.user.service.EmailService;
import com.system.animals.modules.user.service.VerifyCodeService;
import com.system.animals.shared.enums.TypeRole;
import com.system.animals.shared.enums.VerifyCodeResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerifyCodeServiceImpl implements VerifyCodeService {

    private static final int CODE_EXPIRATION_MINUTES = 10;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Value("${verification.code.rate-limit.max-requests:3}")
    private int maxCodeRequests;

    @Value("${verification.code.rate-limit.window-minutes:15}")
    private long rateLimitWindowMinutes;

    private final VerifyCodeRepository codeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final Map<String, Deque<LocalDateTime>> codeRequestHistory = new ConcurrentHashMap<>();

    @Override
    public String generateCode() {
        return String.valueOf(100000 + RANDOM.nextInt(900000));
    }

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        Role role = roleRepository.findByName(TypeRole.ROLE_USER)
                .orElseThrow(RoleNotFoundException::new);

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = userMapper.toEntity(request);
        user.setRole(roles);
        user.setEnable(false);
        user.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);
        sendCode(request.email());
    }

    @Override
    @Transactional
    public void sendCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(EmailNotFoundException::new);

        if (user.isEnable()) {
            throw new IllegalStateException("El usuario ya esta verificado");
        }

        checkRateLimit(email);

        codeRepository.deleteByEmail(email);

        String code = generateCode();

        VerifyCode vc = new VerifyCode();
        vc.setEmail(email);
        vc.setCode(code);
        vc.setExpirationTime(LocalDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES));

        codeRepository.save(vc);
        emailService.sendCode(email, code);
    }

    @Override
    @Transactional
    public VerifyCodeResult verifyCode(String email, String code) {
        VerifyCode vc = codeRepository.findTopByEmailOrderByExpirationTimeDesc(email)
                .orElse(null);

        if (vc == null) {
            return VerifyCodeResult.CODE_NOT_FOUND;
        }

        if (vc.getExpirationTime().isBefore(LocalDateTime.now())) {
            codeRepository.delete(vc);
            return VerifyCodeResult.CODE_EXPIRED;
        }

        if (!vc.getCode().equals(code)) {
            return VerifyCodeResult.CODE_INVALID;
        }

        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            codeRepository.delete(vc);
            return VerifyCodeResult.USER_NOT_FOUND;
        }
        user.setEnable(true);

        userRepository.save(user);
        codeRepository.delete(vc);

        return VerifyCodeResult.VERIFIED;
    }

    private void checkRateLimit(String email) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime windowStart = now.minusMinutes(rateLimitWindowMinutes);
        Deque<LocalDateTime> requests = codeRequestHistory.computeIfAbsent(email, key -> new ArrayDeque<>());

        synchronized (requests) {
            while (!requests.isEmpty() && requests.peekFirst().isBefore(windowStart)) {
                requests.removeFirst();
            }

            if (requests.size() >= maxCodeRequests) {
                throw new RateLimitExceededException();
            }

            requests.addLast(now);
        }
    }

}
