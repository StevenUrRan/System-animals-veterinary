package com.system.animals.modules.invoice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.system.animals.modules.invoice.dto.DetailsInvoiceDto;
import com.system.animals.modules.invoice.entity.DetailsInvoice;

@Mapper(componentModel = "spring")
public interface DetailsInvoiceMapper {

    @Mapping(target = "invoice", ignore = true)
    DetailsInvoice toEntity(DetailsInvoiceDto detailsInvoiceDto);

    DetailsInvoiceDto toDto(DetailsInvoice detailsInvoice);

}
