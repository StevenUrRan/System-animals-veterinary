package com.system.animals.modules.invoice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.system.animals.modules.invoice.entity.DetailsInvoice;

@Repository
public interface DetailsInvoiceRepository
        extends JpaRepository<DetailsInvoice, Long>, JpaSpecificationExecutor<DetailsInvoice> {

    List<DetailsInvoice> findByInvoiceId(Long invoiceId);

}
