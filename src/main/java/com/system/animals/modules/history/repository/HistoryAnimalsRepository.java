package com.system.animals.modules.history.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.system.animals.modules.history.entity.HistoryAnimals;

@Repository
public interface HistoryAnimalsRepository
        extends JpaRepository<HistoryAnimals, Long>, JpaSpecificationExecutor<HistoryAnimals> {

    Optional<HistoryAnimals> findByAnimalsId(Long animalId);

    Optional<HistoryAnimals> findByAnimalsIdAndEnableTrue(Long animalId);

    Optional<HistoryAnimals> findByCodeAndEnableTrue(Long code);

    boolean existsByAnimalsId(Long animalId);

    Page<HistoryAnimals> findAllByEnableTrue(Pageable pageable);

}
