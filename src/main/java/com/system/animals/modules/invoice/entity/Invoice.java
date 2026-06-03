package com.system.animals.modules.invoice.entity;

import java.math.BigDecimal;
import java.util.Set;

import com.system.animals.modules.citation.entity.Citation;
import com.system.animals.modules.user.entity.User;
import com.system.animals.shared.base.BaseEntity;
import com.system.animals.shared.enums.InvoiceStatus;
import com.system.animals.shared.enums.PaymentMethod;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "invoice")
@Entity
public class Invoice extends BaseEntity {

    @Column(nullable = false)
    private Long code;

    @Enumerated(EnumType.STRING)
    @Column(name = "paymentMethod", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "sub_total", nullable = false)
    private BigDecimal subTotal;

    @Column(nullable = false)
    private BigDecimal iva;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private InvoiceStatus invoiceStatus;

    @Default
    @Column(nullable = false)
    private boolean enable = true;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY)
    private Set<DetailsInvoice> detailsInvoices;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citation_id")
    private Citation citation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;
}
