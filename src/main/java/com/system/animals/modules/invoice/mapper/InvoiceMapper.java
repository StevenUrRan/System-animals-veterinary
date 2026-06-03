package com.system.animals.modules.invoice.mapper;

import java.util.Set;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.system.animals.modules.invoice.dto.InvoiceRequestDto;
import com.system.animals.modules.invoice.dto.InvoiceResponseDto;
import com.system.animals.modules.invoice.entity.DetailsInvoice;
import com.system.animals.modules.invoice.entity.Invoice;
import com.system.animals.modules.user.entity.User;
import com.system.animals.shared.base.MapperSupport;

@Mapper(componentModel = "spring", uses = DetailsInvoiceMapper.class)
public interface InvoiceMapper {

    @Mapping(target = "user", source = "userId")
    @Mapping(target = "enable", ignore = true)
    Invoice toEntity(InvoiceRequestDto invoiceDto);

    @Mapping(target = "userId", source = "user.id")
    InvoiceResponseDto toDto(Invoice invoice);

    default User mapUserIdToUser(Long userId) {
        if (userId == null) {
            return null;
        }
        User user = User.builder().build();
        return MapperSupport.withId(user, userId);
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
