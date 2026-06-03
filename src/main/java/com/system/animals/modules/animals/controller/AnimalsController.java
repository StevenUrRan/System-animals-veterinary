package com.system.animals.modules.animals.controller;

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

import com.system.animals.modules.animals.dto.AnimalsDto;
import com.system.animals.modules.animals.service.AnimalsService;
import com.system.animals.shared.valid.ValidationResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/animals")
@Tag(name = "Animales", description = "Gestión de animales: registro, consulta y administración")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AnimalsController {

    private final AnimalsService animalsService;
    private final ValidationResult validationResult;

    @Operation(summary = "Listar todos los animales", description = "Retorna la lista de todos los animales registrados. Requiere autenticación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${controller.animal.list.success}"),
            @ApiResponse(responseCode = "401", description = "${controller.token.missing}"),
            @ApiResponse(responseCode = "403", description = "${controller.forbidden}")
    })
    @GetMapping
    public ResponseEntity<Page<AnimalsDto>> findAll(Pageable pageable) {
        Page<AnimalsDto> animals = animalsService.findAll(pageable);
        return ResponseEntity.ok(animals);
    }

    @Operation(summary = "Crear nuevo animal", description = "Retorna los datos del nuevo animal creado. Requiere autenticación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "${controller.animal.create.success}"),
            @ApiResponse(responseCode = "401", description = "${controller.token.missing}"),
            @ApiResponse(responseCode = "403", description = "${controller.forbidden}"),
            @ApiResponse(responseCode = "400", description = "${controller.animal.create.invalid}")
    })
    @PostMapping
    public ResponseEntity<?> newAnimals(@Valid @RequestBody AnimalsDto animalsDto, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationResult.validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(animalsService.newAnimals(animalsDto));

    }

    @Operation(summary = "Actualizar animal existente", description = "Retorna los datos del animal actualizado. Requiere autenticación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${controller.animal.update.success}"),
            @ApiResponse(responseCode = "401", description = "${controller.token.missing}"),
            @ApiResponse(responseCode = "403", description = "${controller.forbidden}"),
            @ApiResponse(responseCode = "404", description = "${controller.animal.not-found}"),
            @ApiResponse(responseCode = "400", description = "${controller.animal.update.invalid}")
    })
    @PutMapping("/nit")
    public ResponseEntity<?> updateAnimals(
            @Valid @RequestBody AnimalsDto animalsDto,
            BindingResult result,
            @Parameter(description = "NIT del animal a actualizar", example = "1234567890", required = true) @RequestParam Long nit) {
        if (result.hasFieldErrors()) {
            return validationResult.validation(result);
        }

        return ResponseEntity.ok(animalsService.updateAnimals(nit, animalsDto));
    }

    @Operation(summary = "Eliminar animal existente", description = "No retorna ningún valor. Requiere autenticación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "${controller.animal.delete.success}"),
            @ApiResponse(responseCode = "401", description = "${controller.token.missing}"),
            @ApiResponse(responseCode = "403", description = "${controller.forbidden}"),
            @ApiResponse(responseCode = "404", description = "${controller.animal.not-found}")
    })
    @DeleteMapping("/nit")
    public ResponseEntity<Void> deleteAnimals(
            @Parameter(description = "NIT del animal a eliminar", example = "1234567890", required = true) @RequestParam Long nit) {
        animalsService.delete(nit);
        return ResponseEntity.noContent().build();
    }

}