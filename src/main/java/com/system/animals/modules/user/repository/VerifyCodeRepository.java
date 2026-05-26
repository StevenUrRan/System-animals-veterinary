package com.system.animals.modules.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.system.animals.modules.user.entity.VerifyCode;

public interface VerifyCodeRepository extends JpaRepository<VerifyCode, Long>{

    Optional<VerifyCode> findTopByEmailOrderByExpirationTimeDesc(String email);

    void deleteByEmail(String email);

}
