package com.system.animals.modules.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.system.animals.modules.user.entity.Role;
import com.system.animals.shared.enums.TypeRole;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    Optional<Role> findByName(TypeRole name);

    boolean existsByName(TypeRole name);

}
