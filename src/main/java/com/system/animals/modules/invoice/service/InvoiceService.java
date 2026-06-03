package com.system.animals.modules.invoice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.system.animals.modules.invoice.dto.InvoiceRequestDto;
import com.system.animals.modules.invoice.dto.InvoiceResponseDto;
import com.system.animals.shared.enums.InvoiceStatus;

public interface InvoiceService {

    Page<InvoiceResponseDto> findAll(Pageable pageable);

    InvoiceResponseDto findByCode(Long code);

    InvoiceResponseDto newInvoice(InvoiceRequestDto invoiceDto);

    InvoiceResponseDto processPayment(Long code, InvoiceStatus invoiceStatus);

    void deleteInvoice(Long code);

}
