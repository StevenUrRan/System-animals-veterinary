package com.system.animals.modules.animals.controller;

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

import com.system.animals.modules.animals.dto.AnimalsDto;
import com.system.animals.modules.animals.service.AnimalsService;
import com.system.animals.shared.valid.ValidationResult;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/animals")
@RequiredArgsConstructor
public class AnimalsController {

    private final AnimalsService animalsService;
    private final ValidationResult validationResult;

    @GetMapping
    public ResponseEntity<Page<AnimalsDto>> findAll(Pageable pageable) {
        Page<AnimalsDto> animals = animalsService.findAll(pageable);
        return ResponseEntity.ok(animals);
    }

    @PostMapping
    public ResponseEntity<?> newAnimals(@Valid @RequestBody AnimalsDto animalsDto, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationResult.validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(animalsService.newAnimals(animalsDto));
    }

    @PutMapping("/{nit}")
    public ResponseEntity<?> updateAnimals(@Valid @RequestBody AnimalsDto animalsDto, BindingResult result,
            @PathVariable Long nit) {
        if (result.hasFieldErrors()) {
            return validationResult.validation(result);
        }

        return ResponseEntity.ok(animalsService.updateAnimals(nit, animalsDto));
    }

    @DeleteMapping("/{nit}")
    public ResponseEntity<Void> deleteAnimals(@PathVariable Long nit) {
        animalsService.delete(nit);
        return ResponseEntity.noContent().build();
    }

}
