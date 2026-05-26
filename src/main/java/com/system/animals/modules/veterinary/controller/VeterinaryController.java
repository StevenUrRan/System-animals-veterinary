package com.system.animals.modules.veterinary.controller;

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
import com.system.animals.modules.veterinary.dto.VeterinaryDto;
import com.system.animals.modules.veterinary.service.VeterinaryService;
import com.system.animals.shared.valid.ValidationResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/veterinary")
@RequiredArgsConstructor
public class VeterinaryController {

    private final VeterinaryService veterinaryService;
    private final ValidationResult validationResult;

    @GetMapping
    public ResponseEntity<Page<VeterinaryDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(veterinaryService.findAll(pageable));
    }

    @GetMapping("/{license}")
    public ResponseEntity<VeterinaryDto> findByLicense(@PathVariable Long license) {
        return ResponseEntity.ok(veterinaryService.findByNit(license));
    }

    @PostMapping
    public ResponseEntity<?> newVeterinary(@Valid @RequestBody VeterinaryDto veterinaryDto,
            BindingResult result, @PathVariable Long license) {

        if (result.hasFieldErrors()) {
            return validationResult.validation(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(veterinaryService
                .newVeterinary(license, veterinaryDto));
    }

    @PutMapping("/license")
    public ResponseEntity<?> updateVeterinary(@Valid @RequestBody VeterinaryDto veterinaryDto,
            BindingResult result, @PathVariable Long license) {

        if (result.hasFieldErrors()) {
            return validationResult.validation(result);
        }

        return ResponseEntity.ok(veterinaryService.upodateVeterinary(license, veterinaryDto));
    }

    @DeleteMapping("/{license}")
    public ResponseEntity<VeterinaryDto> delete(@PathVariable Long license) {
        return ResponseEntity.ok(veterinaryService.deleteVeterinary(license));
    }

}
