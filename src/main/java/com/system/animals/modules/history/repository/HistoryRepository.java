package com.system.animals.modules.history.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.system.animals.modules.history.entity.HistoryAnimals;

@Repository
public interface HistoryRepository
        extends JpaRepository<HistoryAnimals, Long>, JpaSpecificationExecutor<HistoryAnimals> {

    Optional<HistoryAnimals> findByAnimalsId(Long animalId);

    boolean existsByAnimalsId(Long animalId);

}
