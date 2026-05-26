package com.system.animals.modules.invoice.mapper;

import java.util.Set;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.system.animals.modules.invoice.dto.InvoiceDto;
import com.system.animals.modules.invoice.entity.DetailsInvoice;
import com.system.animals.modules.invoice.entity.Invoice;
import com.system.animals.modules.user.entity.User;
import com.system.animals.shared.base.MapperSupport;

@Mapper(componentModel = "spring", uses = DetailsInvoiceMapper.class)
public interface InvoiceMapper {

    @Mapping(target = "user", source = "userId")
    Invoice toEntity(InvoiceDto invoiceDto);

    @Mapping(target = "userId", source = "user.id")
    InvoiceDto toDto(Invoice invoice);

    default User mapUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        return MapperSupport.withId(User.builder().build(), userId);
    }

    @AfterMapping
    default void linkInvoice(@MappingTarget Invoice invoice) {
        Set<DetailsInvoice> detailsInvoices = invoice.getDetailsInvoices();
        if (detailsInvoices == null) {
            return;
        }
        detailsInvoices.forEach(detail -> detail.setInvoice(invoice));
    }

}
