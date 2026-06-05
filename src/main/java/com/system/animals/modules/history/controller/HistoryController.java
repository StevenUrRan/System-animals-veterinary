package com.system.animals.modules.history.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.system.animals.modules.history.dto.HistoryAnimalsDto;
import com.system.animals.modules.history.service.HistoryAnimalsService;
import com.system.animals.shared.valid.ValidationResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
@Tag(name = "Historial", description = "Gestión de historial: registro, consulta y administración")
@SecurityRequirement(name = "bearerAuth")
public class HistoryController {

    private final HistoryAnimalsService animalsService;
    private final ValidationResult validationResult;

    @Operation(summary = "Listar todas las historias clinicas", description = "Retorna la lista de todos las historias clinicas registrados. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "401", description = ""),
            @ApiResponse(responseCode = "403", description = "")
    })
    @GetMapping
    public ResponseEntity<Page<HistoryAnimalsDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(animalsService.findAll(pageable));
    }

    @Operation(summary = "Listar todas las historias clinicas", description = "Retorna la lista de todos las historias clinicas registrados. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "401", description = ""),
            @ApiResponse(responseCode = "403", description = "")
    })
    @GetMapping("/nit/{nit}")
    public ResponseEntity<HistoryAnimalsDto> findByAnimalsId(@RequestParam Long nit) {
        return ResponseEntity.ok().body(animalsService.findByHistoryId(nit).get());
    }

    @Operation(summary = "", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "401", description = ""),
            @ApiResponse(responseCode = "403", description = "")
    })
    @PostMapping
    public ResponseEntity<?> newHistoty(@Valid @RequestBody HistoryAnimalsDto history, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationResult.validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(animalsService.newHistoryAnimalsDto(history));
    }

    @Operation(summary = "", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "401", description = ""),
            @ApiResponse(responseCode = "403", description = "")
    })
    @PutMapping("/update/{nit}")
    public ResponseEntity<?> updateHistory(@Valid @RequestBody HistoryAnimalsDto historyAnimalsDto,
            BindingResult result, @RequestParam Long nit) {
        if (result.hasFieldErrors()) {
            return validationResult.validation(result);
        }
        return ResponseEntity.ok(animalsService.updateHistoryAnimalsDto(nit, historyAnimalsDto));
    }

    @Operation(summary = "", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "401", description = ""),
            @ApiResponse(responseCode = "403", description = "")
    })
    @DeleteMapping("/delete/{nit}")
    public ResponseEntity<Void> deleteHistory(@RequestParam Long nit) {
        animalsService.deleteHistoryAnimal(nit);
        return ResponseEntity.ok().build();
    }
}
