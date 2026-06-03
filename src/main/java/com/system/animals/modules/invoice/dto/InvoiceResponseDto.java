package com.system.animals.modules.invoice.dto;

import java.math.BigDecimal;
import java.util.Set;

import com.system.animals.shared.enums.InvoiceStatus;
import com.system.animals.shared.enums.PaymentMethod;

public record InvoiceResponseDto(

        Long code,
        
        PaymentMethod paymentMethod,

        BigDecimal subTotal,

        BigDecimal iva,

        BigDecimal total,

        InvoiceStatus invoiceStatus,

        Set<DetailsInvoiceDto> detailsInvoices,

        Long userId,

        Long citationId
) {

}
