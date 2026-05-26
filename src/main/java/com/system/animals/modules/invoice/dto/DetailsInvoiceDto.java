package com.system.animals.modules.invoice.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DetailsInvoiceDto(

        @NotNull(message = "{invoice-detail.price.not-null}")
        @DecimalMin(value = "0.0", inclusive = false, message = "{invoice-detail.price.min}")
        @Digits(integer = 12, fraction = 2, message = "{invoice-detail.price.digits}")
        BigDecimal price,

        @NotBlank(message = "{invoice-detail.description.not-blank}")
        @Size(min = 5, max = 150, message = "{invoice-detail.description.size}")
        String description) {

}
