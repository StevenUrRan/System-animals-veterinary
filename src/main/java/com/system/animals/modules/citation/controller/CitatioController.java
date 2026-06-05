package com.system.animals.modules.citation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.system.animals.modules.citation.dto.CitationCreateDto;
import com.system.animals.modules.citation.dto.CitationDto;
import com.system.animals.modules.citation.service.CitationService;
import com.system.animals.shared.valid.ValidationResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/citation")
@RequiredArgsConstructor
@Tag(name = "Citacion", description = "Gestión de Citas: registro, consulta y administración")
@SecurityRequirement(name = "bearerAuth")
public class CitatioController {

    private final CitationService citationService;
    private final ValidationResult validationResult;

    @Operation(summary = "Obtener todas las citas", description = "Retorna una lista paginada de todas las citas disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de citas retornada correctamente"),
            @ApiResponse(responseCode = "401", description = "Token faltante o expirado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos suficientes")
    })
    @GetMapping
    public ResponseEntity<Page<CitationDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(citationService.findAll(pageable));
    }

    @Operation(summary = "Obtener cita por animal", description = "Retorna la cita asociada a un animal específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cita encontrada correctamente"),
            @ApiResponse(responseCode = "401", description = "Token faltante o expirado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos suficientes"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @GetMapping("/animalsId/{nitAnimals}")
    public ResponseEntity<CitationDto> findByNitAnimals(@PathVariable Long nitAnimals) {
        return ResponseEntity.ok(citationService.findByLicenseAnimal(nitAnimals));
    }

    @Operation(summary = "Crear nueva cita", description = "Registra una nueva cita para un animal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cita creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de cita inválidos"),
            @ApiResponse(responseCode = "401", description = "Token faltante o expirado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos suficientes"),
            @ApiResponse(responseCode = "404", description = "Animal o historia clínica no encontrada")
    })
    @PostMapping("/newCitation/{nit}")
    public ResponseEntity<CitationDto> newCitation(@PathVariable Long nit,
            @Valid @RequestBody CitationCreateDto request,
            BindingResult result) {
        if (result.hasFieldErrors()) {
            validationResult.validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(citationService.newCitation(nit, request));
    }

    @Operation(summary = "Actualizar cita", description = "Actualiza los datos de una cita existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cita actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de cita inválidos"),
            @ApiResponse(responseCode = "401", description = "Token faltante o expirado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos suficientes"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @PutMapping("/update/{nit}")
    public ResponseEntity<CitationDto> updateCitation(@PathVariable Long nit,
            @Valid @RequestBody CitationCreateDto request, BindingResult result) {
        if (result.hasFieldErrors()) {
            validationResult.validation(result);
        }
        return ResponseEntity.ok(citationService.updateCitation(request, nit));
    }

    @Operation(summary = "Cambiar estado de cita", description = "Actualiza el estado de una cita existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado de cita actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "Token faltante o expirado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos suficientes"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @PutMapping("/updateStatus/{nit}")
    public ResponseEntity<CitationDto> updateStatusCitation(@PathVariable Long nit,
            @Valid @RequestBody CitationDto request, BindingResult result) {
        if (result.hasFieldErrors()) {
            validationResult.validation(result);
        }
        return ResponseEntity.ok(citationService.updateStatusCitation(request, nit));
    }

    @Operation(summary = "Eliminar cita", description = "Marca una cita como eliminada (desactiva)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cita eliminada exitosamente"),
            @ApiResponse(responseCode = "401", description = "Token faltante o expirado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos suficientes"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @DeleteMapping("/delete/{nit}")
    public ResponseEntity<Void> deleteCitation(@PathVariable Long nit) {
        citationService.deleteCitation(nit);
        return ResponseEntity.noContent().build();
    }

}
