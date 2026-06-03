package com.system.animals.modules.history.mapper;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.system.animals.modules.animals.entity.Animals;
import com.system.animals.modules.citation.entity.Citation;
import com.system.animals.modules.history.dto.HistoryAnimalsDto;
import com.system.animals.modules.history.entity.HistoryAnimals;
import com.system.animals.shared.base.MapperSupport;

@Mapper(componentModel = "spring")
public interface HistoryAnimalsMapper {

    @Mapping(target = "animals", source = "animalId")
    @Mapping(target = "citations", source = "citationIds")
    @Mapping(target = "enable", ignore = true)
    HistoryAnimals toEntity(HistoryAnimalsDto historyAnimalsDto);

    @Mapping(target = "animalId", source = "animals.id")
    @Mapping(target = "citationIds", source = "citations")
    HistoryAnimalsDto toDto(HistoryAnimals historyAnimals);

    default Animals mapAnimalId(Long animalId) {
        if (animalId == null) {
            return null;
        }
        return MapperSupport.withId(Animals.builder().build(), animalId);
    }

    default Set<Citation> mapCitationIds(Set<Long> citationIds) {
        if (citationIds == null) {
            return null;
        }
        return citationIds.stream()
                .map(this::mapCitationId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    default Citation mapCitationId(Long citationId) {
        if (citationId == null) {
            return null;
        }
        return MapperSupport.withId(new Citation(), citationId);
    }

    default Set<Long> mapCitations(Set<Citation> citations) {
        if (citations == null) {
            return null;
        }
        return citations.stream()
                .map(Citation::getId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
