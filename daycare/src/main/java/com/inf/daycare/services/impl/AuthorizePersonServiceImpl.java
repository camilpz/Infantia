package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.get.GetAuthorizedPersonDto;
import com.inf.daycare.dtos.post.PostAuthorizedPersonDto;
import com.inf.daycare.dtos.put.PutAuthorizedPersonDto;
import com.inf.daycare.mapper.AuthorizedPersonMapper;
import com.inf.daycare.models.AuthorizedPerson;
import com.inf.daycare.repositories.AuthorizedPersonRepository;
import com.inf.daycare.services.AuthorizedPersonService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorizePersonServiceImpl implements AuthorizedPersonService {
    private final AuthorizedPersonRepository authorizedPersonRepository;
    private final AuthorizedPersonMapper authorizedPersonMapper;

    @Override
    @Transactional(readOnly = true)
    public List<GetAuthorizedPersonDto> getAllAuthorizedPeople() {
        List<AuthorizedPerson> authorizedPeople = authorizedPersonRepository.findAll();
        return authorizedPersonMapper.listAuthorizedPersonToGetAuthorizedPersonDtoList(authorizedPeople);
    }

    @Override
    @Transactional(readOnly = true)
    public GetAuthorizedPersonDto getAuthorizedPersonById(Long id) {
        AuthorizedPerson authorizedPerson = authorizedPersonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Persona autorizada no encontrada con ID: " + id));
        return authorizedPersonMapper.authorizedPersonToGetAuthorizedPersonDto(authorizedPerson);
    }

    @Override
    @Transactional
    public GetAuthorizedPersonDto createAuthorizedPerson(PostAuthorizedPersonDto postAuthorizedPersonDto) {
        // Opcional: Validar si ya existe una persona con el mismo DNI antes de intentar guardar
        authorizedPersonRepository.findByDni(postAuthorizedPersonDto.getDni())
                .ifPresent(p -> {
                    throw new DataIntegrityViolationException("Ya existe una persona autorizada con el DNI: " + p.getDni());
                });

        AuthorizedPerson authorizedPerson = authorizedPersonMapper.postAuthorizedPersonDtoToAuthorizedPerson(postAuthorizedPersonDto);
        authorizedPerson = authorizedPersonRepository.save(authorizedPerson);
        return authorizedPersonMapper.authorizedPersonToGetAuthorizedPersonDto(authorizedPerson);
    }

    @Override
    @Transactional
    public GetAuthorizedPersonDto updateAuthorizedPerson(Long id, PutAuthorizedPersonDto putAuthorizedPersonDto) {
        AuthorizedPerson existingPerson = authorizedPersonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Persona autorizada no encontrada con ID: " + id));

        // Si se intenta actualizar el DNI, verificar que no cause duplicados
        if (putAuthorizedPersonDto.getDni() != null && !putAuthorizedPersonDto.getDni().equals(existingPerson.getDni())) {
            authorizedPersonRepository.findByDni(putAuthorizedPersonDto.getDni())
                    .ifPresent(p -> {
                        if (!p.getId().equals(id)) { // Asegurarse de que no es la misma entidad
                            throw new DataIntegrityViolationException("Ya existe otra persona autorizada con el DNI: " + p.getDni());
                        }
                    });
        }

        authorizedPersonMapper.updateAuthorizedPersonFromPutDto(putAuthorizedPersonDto, existingPerson);
        AuthorizedPerson updatedPerson = authorizedPersonRepository.save(existingPerson);
        return authorizedPersonMapper.authorizedPersonToGetAuthorizedPersonDto(updatedPerson);
    }

    @Override
    public void changeStatus(Long id, Boolean status) {
        AuthorizedPerson authorizedPerson = getAuthorizedPersonOrThrow(id);
        authorizedPerson.setActive(status);

        authorizedPersonRepository.save(authorizedPerson);
    }

    @Override
    @Transactional(readOnly = true)
    public GetAuthorizedPersonDto getAuthorizedPersonByDni(String dni) {
        AuthorizedPerson authorizedPerson = authorizedPersonRepository.findByDni(dni)
                .orElseThrow(() -> new EntityNotFoundException("Persona autorizada no encontrada con DNI: " + dni));
        return authorizedPersonMapper.authorizedPersonToGetAuthorizedPersonDto(authorizedPerson);
    }

    //-------------------------------------------------Métodos auxiliares-------------------------------------------------

    public AuthorizedPerson getAuthorizedPersonOrThrow(Long id) {
        return authorizedPersonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Persona autorizada no encontrada con ID: " + id));
    }
}
