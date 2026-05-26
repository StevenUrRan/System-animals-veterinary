package com.system.animals.modules.invoice.dto;

import java.math.BigDecimal;
import java.util.Set;

import com.system.animals.shared.enums.PaymentMethod;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InvoiceDto(

        @NotNull(message = "{invoice.code.not-null}")
        @Positive(message = "{invoice.code.positive}")
        Long code,

        @NotNull(message = "{invoice.payment-method.not-null}")
        PaymentMethod paymentMethod,

        @NotNull(message = "{invoice.sub-total.not-null}")
        @DecimalMin(value = "0.0", message = "{invoice.sub-total.min}")
        @Digits(integer = 12, fraction = 2, message = "{invoice.sub-total.digits}")
        BigDecimal subTotal,

        @NotNull(message = "{invoice.iva.not-null}")
        @DecimalMin(value = "0.0", message = "{invoice.iva.min}")
        @Digits(integer = 12, fraction = 2, message = "{invoice.iva.digits}")
        BigDecimal iva,

        @NotNull(message = "{invoice.total.not-null}")
        @DecimalMin(value = "0.0", message = "{invoice.total.min}")
        @Digits(integer = 12, fraction = 2, message = "{invoice.total.digits}")
        BigDecimal total,

        @Valid
        @NotEmpty(message = "{invoice.details.not-empty}")
        Set<DetailsInvoiceDto> detailsInvoices,

        @NotNull(message = "{invoice.user-id.not-null}")
        @Positive(message = "{invoice.user-id.positive}")
        Long userId
    ) {

}
