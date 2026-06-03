package com.system.animals.modules.citation.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.system.animals.modules.citation.dto.CitationCreateDto;
import com.system.animals.modules.citation.dto.CitationDto;

public interface CitationService {
    Page<CitationDto> findAll(Pageable pageable);

    CitationDto findByLicenseAnimal(Long licenseAnimals);

    CitationDto newCitation(Long licenseAnimals, CitationCreateDto citationDto);

    CitationDto updateCitation(CitationCreateDto citationDto, Long licenseAnimals);

    CitationDto updateStatusCitation(CitationDto citationDto, Long licenseAnimals);

    void deleteCitation(Long licenseAnimals);

}
