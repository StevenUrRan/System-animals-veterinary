package com.system.animals.modules.animals.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.system.animals.modules.animals.entity.Animals;
import com.system.animals.shared.enums.AnimalGender;
import com.system.animals.shared.enums.TypeAnimals;

@Repository
public interface AnimalsRepository extends JpaRepository<Animals, Long>, JpaSpecificationExecutor<Animals> {

    List<Animals> findByNit(Long nit);

    List<Animals> findByNameContainingIgnoreCase(String name);

    List<Animals> findByType(TypeAnimals type);

    List<Animals> findByGender(AnimalGender gender);

    boolean existsByNit(Long nit);

}
