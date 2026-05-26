package com.system.animals.modules.animals.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.system.animals.modules.animals.entity.Animals;
import com.system.animals.shared.enums.AnimalGender;
import com.system.animals.shared.enums.TypeAnimals;

@Repository
public interface AnimalsRepository extends JpaRepository<Animals, Long>, JpaSpecificationExecutor<Animals> {

    Optional<Animals> findByNit(Long nit);

    Optional<Animals> findByNitAndEnableTrue(Long nit);

    List<Animals> findByNameContainingIgnoreCase(String name);

    List<Animals> findByNameContainingIgnoreCaseAndEnableTrue(String name);

    List<Animals> findByType(TypeAnimals type);

    List<Animals> findByTypeAndEnableTrue(TypeAnimals type);

    List<Animals> findByGender(AnimalGender gender);

    List<Animals> findByGenderAndEnableTrue(AnimalGender gender);

    boolean existsByNit(Long nit);

    Page<Animals> findAllByEnableTrue(Pageable pageable);

}
