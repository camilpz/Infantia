package com.inf.daycare.services;

import com.inf.daycare.dtos.get.GetAuthorizedPersonDto;
import com.inf.daycare.dtos.post.PostAuthorizedPersonDto;
import com.inf.daycare.dtos.put.PutAuthorizedPersonDto;

import java.util.List;

public interface AuthorizedPersonService {
    List<GetAuthorizedPersonDto> getAllAuthorizedPeople();
    GetAuthorizedPersonDto getAuthorizedPersonById(Long id);
    GetAuthorizedPersonDto createAuthorizedPerson(PostAuthorizedPersonDto postAuthorizedPersonDto);
    GetAuthorizedPersonDto updateAuthorizedPerson(Long id, PutAuthorizedPersonDto putAuthorizedPersonDto);
    void changeStatus(Long id, Boolean status);

    // Puedes añadir métodos de búsqueda, por ejemplo, por DNI
    GetAuthorizedPersonDto getAuthorizedPersonByDni(String dni);
}
