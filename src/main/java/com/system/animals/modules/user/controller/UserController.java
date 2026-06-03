package com.system.animals.modules.user.controller;

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

import com.system.animals.modules.user.dto.UserRequestDto;
import com.system.animals.modules.user.dto.UserResponseDto;
import com.system.animals.modules.user.dto.UserUpdateDto;
import com.system.animals.modules.user.service.UserService;
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
@RequestMapping("/user")
@Tag(name = "Usuarios", description = "Gestión de usuarios: registro, consulta y administración")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
    private final ValidationResult validationResult;



    @Operation(summary = "Listar todos los usuarios", description = "Retorna la lista de todos los usuarios registrados. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${controller.user.list.success}"),
            @ApiResponse(responseCode = "401", description = "${controller.token.missing}"),
            @ApiResponse(responseCode = "403", description = "${controller.forbidden}")
    })
    @GetMapping("/list")
    public ResponseEntity<Page<UserResponseDto>> findAllUser(Pageable pageable) {
        Page<UserResponseDto> page = userService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Buscar usuario por email", description = "Retorna los datos de un usuario identificado por su dirección de correo. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${controller.user.find.success}"),
            @ApiResponse(responseCode = "401", description = "${controller.token.missing}"),
            @ApiResponse(responseCode = "403", description = "${controller.forbidden}"),
            @ApiResponse(responseCode = "404", description = "${controller.user.find.not-found}")
    })
    @GetMapping("/email")
    public ResponseEntity<?> findByEmail(
            @Parameter(description = "El email es obligatorio...", example = "admin@correo.com", required = true) @RequestParam String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @Operation(summary = "Buscar usuario por nit", description = "Retorna los datos de un usuario identificado nit. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${controller.user.find.success}"),
            @ApiResponse(responseCode = "401", description = "${controller.token.missing}"),
            @ApiResponse(responseCode = "403", description = "${controller.forbidden}"),
            @ApiResponse(responseCode = "404", description = "${controller.user.find.not-found}")
    })
    @GetMapping("/nit")
    public ResponseEntity<?> findByNit(
            @Parameter(description = "El nit(numero unico de identificacion es unico..) es obligatorio", example = "1234567890") @RequestParam Long nit) {
        if (nit == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(userService.findByNit(nit));
    }

    @Operation(summary = "Crear nuevo administrador", description = "Retorna los datos de el nuevo usuario. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "${controller.user.create.success}"),
            @ApiResponse(responseCode = "401", description = "${controller.token.missing}"),
            @ApiResponse(responseCode = "403", description = "${controller.forbidden}"),
            @ApiResponse(responseCode = "400", description = "${controller.user.create.invalid}")
    })
    @PostMapping("/admin")
    public ResponseEntity<?> adminCreate(@Valid @RequestBody UserRequestDto requestDto, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationResult.validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.newAdmin(requestDto));

    }

    @Operation(summary = "Crear nuevo Usuario con un rol especifico asignado...", description = "Retorna los datos de el nuevo usuario. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "${controller.user.create.success}"),
            @ApiResponse(responseCode = "401", description = "${controller.token.missing}"),
            @ApiResponse(responseCode = "403", description = "${controller.forbidden}"),
            @ApiResponse(responseCode = "400", description = "${controller.user.create.invalid}")
    })
    @PostMapping("/user-type")
    public ResponseEntity<?> userTypeCreate(@Valid @RequestBody UserRequestDto requestDto, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationResult.validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.newUserType(requestDto));

    }

    @Operation(summary = "Actualizar usuario existente", description = "Retorna los datos de el usuario actualizado. Requiere cualquiier tipo de rol rol.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${controller.user.update.success}"),
            @ApiResponse(responseCode = "401", description = "${controller.token.missing}"),
            @ApiResponse(responseCode = "403", description = "${controller.forbidden}"),
            @ApiResponse(responseCode = "400", description = "${controller.user.update.invalid}")
    })
    @PutMapping("/nit")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateDto requestDto, BindingResult result,
            @Parameter(description = "El nit(codigo unico de identificación...) es requerido", example = "1234567890") @RequestParam Long nit) {
        if (result.hasFieldErrors()) {
            return validationResult.validation(result);
        }
        return ResponseEntity.ok(userService.update(requestDto, nit));
    }

    @Operation(summary = "Eliminar usuario existente", description = "No retorna ningun valor. Requiere cualquiier tipo de rol rol.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "${controller.user.delete.success}"),
            @ApiResponse(responseCode = "401", description = "${controller.token.missing}"),
            @ApiResponse(responseCode = "403", description = "${controller.forbidden}"),
            @ApiResponse(responseCode = "404", description = "${controller.user.delete.not-found}")
    })
    @DeleteMapping("/nit")
    public ResponseEntity<Void> deleteUser(@RequestParam Long nit) {
        userService.deleteUser(nit);
        return ResponseEntity.noContent().build();
    }

}
