package com.system.animals.modules.citation.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.system.animals.exception.AnimalsNotFoundException;
import com.system.animals.exception.NonBusinessDayException;
import com.system.animals.exception.SaturdayException;
import com.system.animals.exception.SlotInvalidException;
import com.system.animals.exception.VeterinaryUnavailableException;
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
import com.system.animals.modules.veterinary.entity.Veterinary;
import com.system.animals.modules.veterinary.repository.VeterinaryRepository;
import com.system.animals.shared.enums.AppointmentSlot;
import com.system.animals.shared.utils.CodeGenerator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CitationServiceImpl implements CitationService {

    private final CitationMapper citationMapper;
    private final CitationRepository citationRepository;
    private final AnimalsRepository animalsRepository;
    private final HistoryAnimalsRepository historyAnimalsRepository;
    private final VeterinaryRepository veterinaryRepository;
    private final CodeGenerator codeGenerator;

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

        Set<DayOfWeek> businessDays = Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);
        if (!businessDays.contains(day)) {
            if (day == DayOfWeek.SATURDAY) {
                throw new SaturdayException();
            }
            if (day == DayOfWeek.SUNDAY) {
                throw new NonBusinessDayException();
            }
        }

        do {
            codeGenerate = codeGenerator.generateUniqueCode();
            exist = citationRepository.existsByCodeUnique(codeGenerate);
        } while (exist);

        List<Veterinary> availableVeterinarians = veterinaryRepository
                .findAllByEnableTrue();
        Veterinary assignedVeterinarian = null;

        for (Veterinary veterinarian : availableVeterinarians) {
            boolean isAvailable = !citationRepository
                    .existsByVeterinaryIdAndTimeDateAndEnableTrue(veterinarian.getId(), citationDto.timeDate());
            if (isAvailable) {
                assignedVeterinarian = veterinarian;
                break;
            }
        }

        if (assignedVeterinarian == null) {
            throw new VeterinaryUnavailableException();
        }

        Citation newCitation = citationMapper.toEntityVeterinary(citationDto);
        newCitation.setCodeUnique(codeGenerate);
        newCitation.setVeterinary(assignedVeterinarian);
        Set<Animals> animalsSet = new HashSet<>();
        animalsSet.add(animals);
        newCitation.setAnimals(animalsSet);
        newCitation.setHistoryAnimals(historyAnimals);

        Citation save = citationRepository.save(newCitation);

        return citationMapper.toDto(newCitation);

    }

    @Override
    @Transactional
    public CitationDto updateCitation(CitationCreateDto citationDto, Long licenseAnimals) {
        Citation citation = citationRepository.findByAnimalsIdAndEnableTrue(licenseAnimals)
                .orElseThrow(AnimalsNotFoundException::new);

        citationMapper.updateEntityFromDto(citationDto, citation);

        Citation updated = citationRepository.save(citation);
        return citationMapper.toDto(updated);
    }

    @Override
    @Transactional
    public CitationDto updateStatusCitation(CitationDto citationDto, Long licenseAnimals) {
        Citation citation = citationRepository.findByAnimalsIdAndEnableTrue(licenseAnimals)
                .orElseThrow(AnimalsNotFoundException::new);

        citation.setState(citationDto.state());
        citation.setNote(citationDto.note());

        Citation updated = citationRepository.save(citation);
        return citationMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteCitation(Long licenseAnimals) {
        Citation citation = citationRepository.findByAnimalsIdAndEnableTrue(licenseAnimals)
                .orElseThrow(AnimalsNotFoundException::new);
        citation.setEnable(false);
        citationRepository.save(citation);
    }

}
