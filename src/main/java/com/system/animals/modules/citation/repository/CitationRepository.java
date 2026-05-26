package com.system.animals.modules.citation.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.system.animals.modules.citation.entity.Citation;
import com.system.animals.shared.enums.TypeState;

@Repository
public interface CitationRepository extends JpaRepository<Citation, Long>, JpaSpecificationExecutor<Citation> {

    List<Citation> findByState(TypeState state);

    List<Citation> findByStateAndEnableTrue(TypeState state);

    List<Citation> findByVeterinaryId(Long veterinaryId);

    List<Citation> findByVeterinaryIdAndEnableTrue(Long veterinaryId);

    List<Citation> findByHistoryAnimalsId(Long historyAnimalsId);

    List<Citation> findByHistoryAnimalsIdAndEnableTrue(Long historyAnimalsId);

    List<Citation> findByTimeDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Citation> findByTimeDateBetweenAndEnableTrue(LocalDateTime startDate, LocalDateTime endDate);

    Page<Citation> findAllByEnableTrue(Pageable pageable);

}
