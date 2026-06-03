package com.system.animals.modules.veterinary.controller;

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

import com.system.animals.modules.veterinary.dto.VeterinaryDto;
import com.system.animals.modules.veterinary.service.VeterinaryService;
import com.system.animals.shared.valid.ValidationResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/veterinary")
@RequiredArgsConstructor
@Tag(name = "veterinary", description = "\"Gestión de doctores(Veterinarios): registro, consulta y administración")
@SecurityRequirement(name = "bearerAuth")
public class VeterinaryController {

    private final VeterinaryService veterinaryService;
    private final ValidationResult validationResult;

    @Operation(summary = "Listar todos los Veterinarios", description = "Retorna la lista de todos los Veterinarios registrados. Requiere autenticación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "401", description = ""),
            @ApiResponse(responseCode = "403", description = "")
    })
    @GetMapping
    public ResponseEntity<Page<VeterinaryDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(veterinaryService.findAll(pageable));
    }

    @Operation(summary = "Listar todos los Veterinarios", description = "Retorna la lista de todos los Veterinarios registrados. Requiere autenticación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "401", description = ""),
            @ApiResponse(responseCode = "403", description = "")
    })
    @GetMapping("/license")
    public ResponseEntity<VeterinaryDto> findByLicense(@RequestParam Long license) {
        return ResponseEntity.ok(veterinaryService.findByNit(license));
    }

    @Operation(summary = "Listar todos los Veterinarios", description = "Retorna la lista de todos los Veterinarios registrados. Requiere autenticación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "401", description = ""),
            @ApiResponse(responseCode = "403", description = "")
    })
    @PostMapping
    public ResponseEntity<?> newVeterinary(@Valid @RequestBody VeterinaryDto veterinaryDto,
            BindingResult result, @RequestParam Long license) {

        if (result.hasFieldErrors()) {
            return validationResult.validation(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(veterinaryService
                .newVeterinary(license, veterinaryDto));
    }

    @Operation(summary = "Listar todos los Veterinarios", description = "Retorna la lista de todos los Veterinarios registrados. Requiere autenticación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "401", description = ""),
            @ApiResponse(responseCode = "403", description = "")
    })
    @PutMapping("/license")
    public ResponseEntity<?> updateVeterinary(@Valid @RequestBody VeterinaryDto veterinaryDto,
            BindingResult result, @RequestParam Long license) {

        if (result.hasFieldErrors()) {
            return validationResult.validation(result);
        }

        return ResponseEntity.ok(veterinaryService.upodateVeterinary(license, veterinaryDto));
    }

    @Operation(summary = "Listar todos los Veterinarios", description = "Retorna la lista de todos los Veterinarios registrados. Requiere autenticación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "401", description = ""),
            @ApiResponse(responseCode = "403", description = "")
    })
    @DeleteMapping("/license")
    public ResponseEntity<VeterinaryDto> delete(@RequestParam Long license) {
        return ResponseEntity.ok(veterinaryService.deleteVeterinary(license));
    }

}
