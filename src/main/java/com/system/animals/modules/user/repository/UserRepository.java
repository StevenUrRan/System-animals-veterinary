package com.system.animals.modules.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.system.animals.modules.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNit(Long nit);

    boolean existsByEmail(String email);

    boolean existsByNit(Long nit);

    boolean findByEnable(boolean enable);

}
