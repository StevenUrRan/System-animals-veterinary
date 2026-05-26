package com.system.animals.modules.citation.mapper;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.system.animals.modules.animals.entity.Animals;
import com.system.animals.modules.citation.dto.CitationDto;
import com.system.animals.modules.citation.entity.Citation;
import com.system.animals.modules.history.entity.HistoryAnimals;
import com.system.animals.modules.veterinary.entity.Veterinary;
import com.system.animals.shared.base.MapperSupport;

@Mapper(componentModel = "spring")
public interface CitationMapper {

    @Mapping(target = "veterinary", source = "veterinaryId")
    @Mapping(target = "animals", source = "animalIds")
    @Mapping(target = "historyAnimals", source = "historyAnimalsId")
    Citation toEntity(CitationDto citationDto);

    @Mapping(target = "veterinaryId", source = "veterinary.id")
    @Mapping(target = "animalIds", source = "animals")
    @Mapping(target = "historyAnimalsId", source = "historyAnimals.id")
    CitationDto toDto(Citation citation);

    default Veterinary mapVeterinaryId(Long veterinaryId) {
        if (veterinaryId == null) {
            return null;
        }
        return MapperSupport.withId(Veterinary.builder().build(), veterinaryId);
    }

    default Set<Animals> mapAnimalIds(Set<Long> animalIds) {
        if (animalIds == null) {
            return null;
        }
        return animalIds.stream()
                .map(this::mapAnimalId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    default Animals mapAnimalId(Long animalId) {
        if (animalId == null) {
            return null;
        }
        return MapperSupport.withId(Animals.builder().build(), animalId);
    }

    default HistoryAnimals mapHistoryAnimalsId(Long historyAnimalsId) {
        if (historyAnimalsId == null) {
            return null;
        }
        return MapperSupport.withId(HistoryAnimals.builder().build(), historyAnimalsId);
    }

    default Set<Long> mapAnimals(Set<Animals> animals) {
        if (animals == null) {
            return null;
        }
        return animals.stream()
                .map(Animals::getId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
