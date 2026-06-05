package com.system.animals.modules.invoice.controller;

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

import com.system.animals.modules.invoice.dto.InvoiceRequestDto;
import com.system.animals.modules.invoice.dto.InvoiceResponseDto;
import com.system.animals.modules.invoice.service.InvoiceService;
import com.system.animals.shared.enums.InvoiceStatus;
import com.system.animals.shared.valid.ValidationResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/Invoice")
@Tag(name = "Factura", description = "Gestión de Factura: registro, consulta y administración")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final ValidationResult validationResult;

    @Operation(summary = "", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "401", description = ""),
            @ApiResponse(responseCode = "403", description = "")
    })
    @GetMapping
    public ResponseEntity<Page<InvoiceResponseDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(invoiceService.findAll(pageable));
    }

    @Operation(summary = "", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "401", description = ""),
            @ApiResponse(responseCode = "403", description = "")
    })
    @GetMapping("code/{code}")
    public ResponseEntity<InvoiceResponseDto> findByCode(@RequestParam Long code) {
        return ResponseEntity.ok(invoiceService.findByCode(code));
    }

    @Operation(summary = "", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "401", description = ""),
            @ApiResponse(responseCode = "403", description = "")
    })
    @PostMapping
    public ResponseEntity<InvoiceResponseDto> newInvoice(@Valid @RequestBody InvoiceRequestDto requestDto,
            BindingResult result) {
        if (result.hasFieldErrors()) {
            validationResult.validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceService.newInvoice(requestDto));
    }

    @Operation(summary = "", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "401", description = ""),
            @ApiResponse(responseCode = "403", description = "")
    })
    @PutMapping("/processPayment/{code}")
    public ResponseEntity<InvoiceResponseDto> processPayment(@RequestParam Long code, BindingResult result,
            @RequestBody InvoiceStatus invoiceStatus) {
        if (result.hasFieldErrors()) {
            validationResult.validation(result);
        }

        return ResponseEntity.ok(invoiceService.processPayment(code, invoiceStatus));
    }

    @Operation(summary = "", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "401", description = ""),
            @ApiResponse(responseCode = "403", description = "")
    })
    @DeleteMapping("/delete/{nit}")
    public ResponseEntity<Void> delete(@RequestParam Long code) {
        invoiceService.deleteInvoice(code);
        return ResponseEntity.ok().build();
    }
}
