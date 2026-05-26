package com.system.animals.modules.history.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record HistoryAnimalsDto(

        @NotBlank(message = "{history.background.not-blank}")
        @Size(min = 8, max = 100, message = "{history.background.size}")
        String background,

        @NotNull(message = "{history.animal-id.not-null}")
        @Positive(message = "{history.animal-id.positive}")
        Long animalId,

        Set<@Positive(message = "{history.citation-id.positive}") Long> citationIds) {

}
