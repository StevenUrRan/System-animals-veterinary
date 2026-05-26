package com.system.animals.modules.user.controller;

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

import com.system.animals.modules.user.dto.RoleDto;
import com.system.animals.modules.user.service.RoleService;
import com.system.animals.shared.enums.TypeRole;
import com.system.animals.shared.valid.ValidationResult;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final ValidationResult validationResult;

    @GetMapping
    public ResponseEntity<Page<RoleDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(roleService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody RoleDto roleDto, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationResult.validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.newRole(roleDto));
    }

    @PutMapping("/{name}")
    public ResponseEntity<?> update(
            @PathVariable TypeRole name,
            @Valid @RequestBody RoleDto roleDto,
            BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationResult.validation(result);
        }
        return ResponseEntity.ok(roleService.updateRole(roleDto, name));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> delete(@PathVariable TypeRole name) {
        roleService.deleteRole(name);
        return ResponseEntity.noContent().build();
    }
}
