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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.system.animals.modules.user.dto.RoleDto;
import com.system.animals.modules.user.service.RoleService;
import com.system.animals.shared.enums.TypeRole;
import com.system.animals.shared.valid.ValidationResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/roles")
@Tag(name = "Roles", description = "Gestión de roles: creación, consulta y administración")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class RoleController {

    private final RoleService roleService;
    private final ValidationResult validationResult;



    @Operation(summary = "Listar todos los roles", description = "Retorna la lista de todos los roles registrados. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${controller.role.list.success}"),
            @ApiResponse(responseCode = "401", description = "${controller.token.missing}"),
            @ApiResponse(responseCode = "403", description = "${controller.forbidden}")
    })
    @GetMapping
    public ResponseEntity<Page<RoleDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(roleService.findAll(pageable));
    }

    @Operation(summary = "Crear nuevo rol", description = "Retorna los datos del nuevo rol creado. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "${controller.role.create.success}"),
            @ApiResponse(responseCode = "401", description = "${controller.token.missing}"),
            @ApiResponse(responseCode = "403", description = "${controller.forbidden}"),
            @ApiResponse(responseCode = "400", description = "${controller.role.create.invalid}")
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody RoleDto roleDto, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationResult.validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.newRole(roleDto));
    }

    @Operation(summary = "Actualizar rol existente", description = "Retorna los datos del rol actualizado. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${controller.role.update.success}"),
            @ApiResponse(responseCode = "401", description = "${controller.token.missing}"),
            @ApiResponse(responseCode = "403", description = "${controller.forbidden}"),
            @ApiResponse(responseCode = "404", description = "${controller.role.not-found}"),
            @ApiResponse(responseCode = "400", description = "${controller.role.update.invalid}")
    })
    @PutMapping("/name")
    public ResponseEntity<?> update(
            @Parameter(description = "Nombre del rol a actualizar", example = "ADMIN", required = true)
            @RequestParam TypeRole name,
            @Valid @RequestBody RoleDto roleDto,
            BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationResult.validation(result);
        }
        return ResponseEntity.ok(roleService.updateRole(roleDto, name));
    }

    @Operation(summary = "Eliminar rol existente", description = "No retorna ningún valor. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "${controller.role.delete.success}"),
            @ApiResponse(responseCode = "401", description = "${controller.token.missing}"),
            @ApiResponse(responseCode = "403", description = "${controller.forbidden}"),
            @ApiResponse(responseCode = "404", description = "${controller.role.not-found}")
    })
    @DeleteMapping("/name")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Nombre del rol a eliminar", example = "ADMIN", required = true)
            @RequestParam TypeRole name) {
        roleService.deleteRole(name);
        return ResponseEntity.noContent().build();
    }
}