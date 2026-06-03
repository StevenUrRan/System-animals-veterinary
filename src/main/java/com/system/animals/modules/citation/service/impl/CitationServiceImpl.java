package com.system.animals.modules.citation.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.system.animals.exception.AnimalsNotFoundException;
import com.system.animals.exception.DayNotFoundException;
import com.system.animals.exception.NonBusinessDayException;
import com.system.animals.exception.SaturdayException;
import com.system.animals.exception.SlotInvalidException;
import com.system.animals.modules.animals.entity.Animals;
import com.system.animals.modules.animals.repository.AnimalsRepository;
import com.system.animals.modules.citation.dto.CitationCreateDto;
import com.system.animals.modules.citation.dto.CitationDto;
import com.system.animals.modules.citation.entity.Citation;
import com.system.animals.modules.citation.mapper.CitationMapper;
import com.system.animals.modules.citation.repository.CitationRepository;
import com.system.animals.modules.citation.service.CitationService;
import com.system.animals.modules.history.entity.HistoryAnimals;
import com.system.animals.modules.history.repository.HistoryAnimalsRepository;
import com.system.animals.modules.veterinary.repository.VeterinaryRepository;
import com.system.animals.shared.enums.AppointmentSlot;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CitationServiceImpl implements CitationService {

    private final CitationMapper citationMapper;
    private final CitationRepository citationRepository;
    private final AnimalsRepository animalsRepository;
    private final HistoryAnimalsRepository historyAnimalsRepository;
    private final VeterinaryRepository veterinaryRepository;
    private final Random random;

    @Override
    @Transactional(readOnly = true)
    public Page<CitationDto> findAll(Pageable pageable) {
        Page<Citation> toEntity = citationRepository.findAllByEnableTrue(pageable);
        return toEntity.map(citationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CitationDto findByLicenseAnimal(Long licenseAnimals) {

        Citation citation = citationRepository.findByAnimalsIdAndEnableTrue(licenseAnimals)
                .orElseThrow(AnimalsNotFoundException::new);
        return citationMapper.toDto(citation);
    }

    @Override
    @Transactional
    public CitationDto newCitation(Long licenseAnimals, CitationCreateDto citationDto) {

        Long codeGenerate;
        boolean exist;
        DayOfWeek day = citationDto.timeDate().getDayOfWeek();

        HistoryAnimals historyAnimals = historyAnimalsRepository.findByAnimalsIdAndEnableTrue(licenseAnimals)
                .orElseThrow(AnimalsNotFoundException::new);

        Animals animals = animalsRepository.findByNitAndEnableTrue(licenseAnimals)
                .orElseThrow(AnimalsNotFoundException::new);

        LocalDateTime requestedDateTime = citationDto.timeDate();
        LocalTime requestedTime = requestedDateTime.toLocalTime();

        boolean validSlot = Arrays.stream(AppointmentSlot.values())
                .anyMatch(slot -> slot.getTime().equals(requestedTime));
        if (!validSlot) {
            throw new SlotInvalidException();
        }
        if (day != DayOfWeek.MONDAY || day != DayOfWeek.TUESDAY || day != DayOfWeek.WEDNESDAY
                || day != DayOfWeek.THURSDAY || day != DayOfWeek.FRIDAY) {
            throw new DayNotFoundException();
        }
        if (day == DayOfWeek.SATURDAY) {
            throw new SaturdayException();
        }
        if (day == DayOfWeek.SUNDAY) {
            throw new NonBusinessDayException();
        }
        do {

            codeGenerate = 100000000 + random.nextLong(900000000);
            exist = citationRepository.existsByCodeUnique(codeGenerate);

        } while (exist);

        // boolean occupied =
        // citationRepository.existsByVeterinaryIdAndDateAndTimeAndEnableTrue(
        // citationDto.veterinaryId(),
        // citationDto.timeDate());

        // if (occupied) {
        // throw new SlotInvalidException();
        // }
        Citation newCitation = citationMapper.toEntityVeterinary(citationDto);
        newCitation.setCodeUnique(codeGenerate);
        Set<Animals> animalsSet = new HashSet<>();
        animalsSet.add(animals);
        newCitation.setAnimals(animalsSet);
        newCitation.setHistoryAnimals(historyAnimals);

        Citation save = citationRepository.save(newCitation);

        return citationMapper.toDto(newCitation);

    }

    @Override
    public CitationDto updateCitation(CitationCreateDto citationDto, Long licenseAnimals) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCitation'");
    }

    @Override
    public CitationDto updateStatusCitation(CitationDto citationDto, Long licenseAnimals) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateStatusCitation'");
    }

    @Override
    public void deleteCitation(Long licenseAnimals) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteCitation'");
    }

}
