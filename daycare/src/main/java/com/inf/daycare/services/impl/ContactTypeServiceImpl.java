package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.get.GetContactTypeDto;
import com.inf.daycare.mapper.ContactTypeMapper;
import com.inf.daycare.models.ContactType;
import com.inf.daycare.repositories.ContactTypeRepository;
import com.inf.daycare.services.ContactTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactTypeServiceImpl implements ContactTypeService {

    private final ContactTypeRepository contactTypeRepository;
    private final ContactTypeMapper contactTypeMapper;

    @Override
    public List<GetContactTypeDto> getAllContactTypes(@Nullable String name) {
        List<ContactType> contactTypes;
        if (name != null && !name.isEmpty()) {
            //Nombre no nulo ni vacío, se obtienen los tipos de contacto que coincidan con el nombre
            contactTypes = contactTypeRepository.findAllByName(name.toUpperCase());
        } else {
            //Nombre nulo o vacío, se obtienen todos los tipos de contacto
            contactTypes = contactTypeRepository.findAll();
        }

        return contactTypeMapper.contactTypeListToGetContactTypeDtoList(contactTypes);
    }
}
