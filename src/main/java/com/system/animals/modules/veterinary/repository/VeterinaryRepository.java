package com.system.animals.modules.veterinary.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.system.animals.modules.veterinary.entity.Veterinary;

@Repository
public interface VeterinaryRepository extends JpaRepository<Veterinary, Long>, JpaSpecificationExecutor<Veterinary> {

    Optional<Veterinary> findByUserId(Long userId);

    Optional<Veterinary> findByLicense(Long license);

    Optional<Veterinary> findByLicenseAndEnableTrue(Long license);

    Optional<Veterinary> findByIdAndEnableTrue(Long id);

    Page<Veterinary> findAllByEnableTrue(Pageable pageable);

    boolean existsByUserId(Long userId);

    boolean existsByUserNit(Long nit);

    java.util.List<Veterinary> findAllByEnableTrue();

}
