package com.system.animals.modules.invoice.service.impl;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.system.animals.exception.CitationNotFoundException;
import com.system.animals.exception.InvoiceNotFoundException;
import com.system.animals.exception.StatusInvoiceInvalidException;
import com.system.animals.exception.UserNotFoundException;
import com.system.animals.modules.citation.entity.Citation;
import com.system.animals.modules.citation.repository.CitationRepository;
import com.system.animals.modules.invoice.dto.InvoiceRequestDto;
import com.system.animals.modules.invoice.dto.InvoiceResponseDto;
import com.system.animals.modules.invoice.entity.DetailsInvoice;
import com.system.animals.modules.invoice.entity.Invoice;
import com.system.animals.modules.invoice.mapper.DetailsInvoiceMapper;
import com.system.animals.modules.invoice.mapper.InvoiceMapper;
import com.system.animals.modules.invoice.repository.DetailsInvoiceRepository;
import com.system.animals.modules.invoice.repository.InvoiceRepository;
import com.system.animals.modules.invoice.service.InvoiceService;
import com.system.animals.modules.user.entity.User;
import com.system.animals.modules.user.repository.UserRepository;
import com.system.animals.shared.enums.InvoiceStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final DetailsInvoiceRepository detailsInvoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final DetailsInvoiceMapper detailsInvoiceMapper;
    private final UserRepository userRepository;
    private final CitationRepository citationRepository;
    private final Random random;

    private Long code;
    private boolean active;

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceResponseDto> findAll(Pageable pageable) {

        Page<Invoice> toEntity = invoiceRepository.findAllByEnableTrue(pageable);
        return toEntity.map(invoiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponseDto findByCode(Long code) {
        Invoice invoice = invoiceRepository.findByCodeAndEnableTrue(code)
                .orElseThrow(InvoiceNotFoundException::new);

        return invoiceMapper.toDto(invoice);
    }

    @Override
    @Transactional
    public InvoiceResponseDto newInvoice(InvoiceRequestDto invoiceDto) {

        User user = userRepository.findByNitAndEnableTrue(invoiceDto.userId())
                .orElseThrow(UserNotFoundException::new);

        Citation citation = citationRepository.findByCodeUniqueAndEnableTrue(invoiceDto.citationId())
                .orElseThrow(CitationNotFoundException::new);

        Set<DetailsInvoice> details = invoiceDto.detailsInvoices().stream().map(detailsInvoiceMapper::toEntity)
                .collect(Collectors.toSet());

        do {
            code = 100000000 + random.nextLong(900000000);
            active = citationRepository.existsByCodeUnique(code);

        } while (active);

        Invoice invoice = invoiceMapper.toEntity(invoiceDto);
        invoice.setDetailsInvoices(details);
        invoice.setCitation(citation);
        invoice.setUser(user);
        invoice.setCode(code);

        Invoice save = invoiceRepository.save(invoice);
        return invoiceMapper.toDto(save);
    }

    @Override
    @Transactional
    public InvoiceResponseDto processPayment(Long code, InvoiceStatus invoiceStatus) {

        Invoice invoice = invoiceRepository.findByCodeAndEnableTrue(code)
                .orElseThrow(InvoiceNotFoundException::new);

        invoice.getInvoiceStatus();
        if (invoice.getInvoiceStatus().equals(invoiceStatus) || invoiceStatus.equals(InvoiceStatus.PAID)) {
            throw new StatusInvoiceInvalidException();
        }

        invoice.setInvoiceStatus(invoiceStatus);
        Invoice save = invoiceRepository.save(invoice);
        return invoiceMapper.toDto(invoice);

    }

    @Override
    @Transactional
    public void deleteInvoice(Long code) {

        Invoice invoice = invoiceRepository.findByCodeAndEnableTrue(code)
                .orElseThrow(InvoiceNotFoundException::new);
        invoice.setEnable(false);
        invoiceRepository.save(invoice);

    }
}