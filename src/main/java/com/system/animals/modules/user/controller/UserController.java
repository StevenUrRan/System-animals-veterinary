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

import com.system.animals.modules.user.dto.UserRequestDto;
import com.system.animals.modules.user.dto.UserResponseDto;
import com.system.animals.modules.user.dto.UserUpdateDto;
import com.system.animals.modules.user.service.UserService;
import com.system.animals.shared.valid.ValidationResult;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ValidationResult validationResult;

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> findAllUser(Pageable pageable) {
        Page<UserResponseDto> page = userService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("email/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("nit/{nit}")
    public ResponseEntity<?> findByNit(@PathVariable Long nit) {
        if (nit == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(userService.findByNit(nit));
    }

    @PostMapping("/admin")
    public ResponseEntity<?> adminCreate(@Valid @RequestBody UserRequestDto requestDto, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationResult.validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.newAdmin(requestDto));

    }

    @PostMapping("/user-type")
    public ResponseEntity<?> userTypeCreate(@Valid @RequestBody UserRequestDto requestDto, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationResult.validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.newUserType(requestDto));

    }

    @PutMapping("/{nit}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long nit,
            @Valid @RequestBody UserUpdateDto requestDto,
            BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationResult.validation(result);
        }
        return ResponseEntity.ok(userService.update(requestDto, nit));
    }

    @DeleteMapping("/{nit}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long nit) {
        userService.deleteUser(nit);
        return ResponseEntity.noContent().build();
    }

}
