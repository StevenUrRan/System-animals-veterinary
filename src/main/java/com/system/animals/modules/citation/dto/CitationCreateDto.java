package com.system.animals.modules.citation.dto;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.system.animals.shared.enums.TypeState;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CitationCreateDto(
        Long codeUnique,

        @NotBlank(message = "{citation.reason.not-blank}")
        @Size(min = 8, max = 100, message = "{citation.reason.size}")
        String reason,

        @NotNull(message = "{citation.state.not-null}")
        TypeState state,

        @NotBlank(message = "{citation.note.not-blank}")
        @Size(min = 8, max = 200, message = "{citation.note.size}")
        String note,

        @NotNull(message = "{citation.time-date.not-null}")
        @FutureOrPresent(message = "{citation.time-date.future-or-present}")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime timeDate,

        @NotEmpty(message = "{citation.animals.not-empty}")
        Set<@Positive(message = "{citation.animal-id.positive}") Long> animalIds,

        @Positive(message = "{citation.history-id.positive}")
        Long historyAnimalsId)
{

}
