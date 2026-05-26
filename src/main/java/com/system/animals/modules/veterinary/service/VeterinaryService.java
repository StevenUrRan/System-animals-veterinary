package com.system.animals.modules.veterinary.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.system.animals.modules.veterinary.dto.VeterinaryDto;

public interface VeterinaryService {

    Page<VeterinaryDto> findAll(Pageable pageable);

    VeterinaryDto findByNit(Long license);

    VeterinaryDto newVeterinary(Long nit, VeterinaryDto veterinaryDto);

    VeterinaryDto upodateVeterinary(Long nit, VeterinaryDto veterinaryDto);

    VeterinaryDto deleteVeterinary(Long nit);

}
