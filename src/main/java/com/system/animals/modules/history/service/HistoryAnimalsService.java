package com.system.animals.modules.history.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.system.animals.modules.history.dto.HistoryAnimalsDto;

public interface HistoryAnimalsService {

    Page<HistoryAnimalsDto> findAll(Pageable pageable);

    Optional<HistoryAnimalsDto> findByHistoryId(Long animalsid);

    HistoryAnimalsDto newHistoryAnimalsDto(HistoryAnimalsDto historyAnimalsDto);

    HistoryAnimalsDto updateHistoryAnimalsDto(Long animalId,HistoryAnimalsDto historyAnimalsDto);

    void deleteHistoryAnimal(Long animalsId);

}
