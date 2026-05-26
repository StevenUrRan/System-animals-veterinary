package com.system.animals.modules.invoice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.system.animals.modules.invoice.entity.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long>, JpaSpecificationExecutor<Invoice> {

    Optional<Invoice> findByCode(Long code);

    Optional<Invoice> findByCodeAndEnableTrue(Long code);

    boolean existsByCode(Long code);

    List<Invoice> findByUserId(Long userId);

    List<Invoice> findByUserIdAndEnableTrue(Long userId);

    Page<Invoice> findAllByEnableTrue(Pageable pageable);

}
