package com.system.animals.modules.history.service.impl;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.system.animals.exception.AnimalsNotFoundException;
import com.system.animals.exception.HistoryAlreadyExistsException;
import com.system.animals.exception.HistoryAnimalsNotFound;
import com.system.animals.exception.HistoryAnimalsNotFoundException;
import com.system.animals.exception.ValueDuplicateException;
import com.system.animals.modules.animals.entity.Animals;
import com.system.animals.modules.animals.repository.AnimalsRepository;
import com.system.animals.modules.citation.entity.Citation;
import com.system.animals.modules.citation.repository.CitationRepository;
import com.system.animals.modules.history.dto.HistoryAnimalsDto;
import com.system.animals.modules.history.entity.HistoryAnimals;
import com.system.animals.modules.history.mapper.HistoryAnimalsMapper;
import com.system.animals.modules.history.repository.HistoryAnimalsRepository;
import com.system.animals.modules.history.service.HistoryAnimalsService;
import com.system.animals.shared.utils.CodeGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoryAnimalsServiceImpl implements HistoryAnimalsService {

    private final CodeGenerator codeGenerator;
    private final HistoryAnimalsMapper historyAnimalsMapper;
    private final HistoryAnimalsRepository historyAnimalsRepository;
    private final AnimalsRepository animalsRepository;
    private final CitationRepository citationRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<HistoryAnimalsDto> findAll(Pageable pageable) {
        Page<HistoryAnimals> toEntity = historyAnimalsRepository.findAllByEnableTrue(pageable);
        return toEntity.map(history -> new HistoryAnimalsDto(
                history.getCode(),
                history.getDescription(),
                history.getAnimals().getId(),
                history.getCitations().stream().map(h -> h.getId()).collect(Collectors.toSet())));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HistoryAnimalsDto> findByHistoryId(Long animalsId) {

        Animals animals = animalsRepository.findByNitAndEnableTrue(animalsId)
                .orElseThrow(AnimalsNotFoundException::new);
        HistoryAnimals historyAnimals = historyAnimalsRepository.findByAnimalsIdAndEnableTrue(animalsId)
                .orElseThrow(HistoryAnimalsNotFound::new);

        return Optional.ofNullable(historyAnimalsMapper.toDto(historyAnimals));

    }

    @Override
    @Transactional
    public HistoryAnimalsDto newHistoryAnimalsDto(HistoryAnimalsDto historyAnimalsDto) {

        Long codeGenerate;
        boolean exist;

        Animals animals = animalsRepository.findByNitAndEnableTrue(historyAnimalsDto.animalId())
                .orElseThrow(AnimalsNotFoundException::new);

        if (historyAnimalsRepository.existsByAnimalsId(animals.getId())) {
            throw new HistoryAlreadyExistsException();
        }

        HistoryAnimals historyAnimals = historyAnimalsMapper.toEntity(historyAnimalsDto);

        do {
            codeGenerate = codeGenerator.generateUniqueCode();
            exist = citationRepository.existsByCodeUnique(codeGenerate);
        } while (exist);

        Set<Citation> citation = citationRepository.findByCodeUniqueInAndEnableTrue(historyAnimalsDto.citationIds());
        historyAnimals.setCitations(citation);

        historyAnimals.setAnimals(animals);

        return historyAnimalsMapper.toDto(historyAnimalsRepository.save(historyAnimals));
    }

    @Override
    @Transactional
    public HistoryAnimalsDto updateHistoryAnimalsDto(Long code, HistoryAnimalsDto historyAnimalsDto) {

        HistoryAnimals historyAnimals = historyAnimalsRepository
                .findByCodeAndEnableTrue(code).orElseThrow(HistoryAnimalsNotFoundException::new);
        Animals animals = animalsRepository.findByNitAndEnableTrue(historyAnimalsDto.animalId())
                .orElseThrow(AnimalsNotFoundException::new);

        HistoryAnimals history = historyAnimalsMapper.toEntity(historyAnimalsDto);
        if (!animals.getNit().equals(history.getAnimals().getNit())) {
            throw new ValueDuplicateException();
        }

        historyAnimals.setDescription(history.getDescription());
        historyAnimals.setAnimals(animals);

        return historyAnimalsMapper.toDto(historyAnimalsRepository.save(historyAnimals));

    }

    @Override
    @Transactional
    public void deleteHistoryAnimal(Long code) {

        HistoryAnimals historyAnimals = historyAnimalsRepository.findByCodeAndEnableTrue(code)
                .orElseThrow(HistoryAnimalsNotFoundException::new);

        historyAnimals.setEnable(false);
        historyAnimalsRepository.save(historyAnimals);
    }

}
