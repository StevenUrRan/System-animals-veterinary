package com.system.animals.modules.user.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.system.animals.modules.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndEnableTrue(String email);

    Optional<User> findByNit(Long nit);

    Optional<User> findByNitAndEnableTrue(Long nit);

    Page<User> findAllByEnableTrue(Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByNit(Long nit);

    boolean findByEnable(boolean enable);

}
