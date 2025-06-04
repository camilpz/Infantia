package com.inf.daycare.mapper;

import com.inf.daycare.dtos.get.GetContactDto;
import com.inf.daycare.dtos.get.GetUserDto;
import com.inf.daycare.dtos.post.PostDirectorDto;
import com.inf.daycare.dtos.post.PostTeacherDto;
import com.inf.daycare.dtos.post.PostTutorDto;
import com.inf.daycare.dtos.post.PostUserDto;
import com.inf.daycare.models.Contact;
import com.inf.daycare.models.DocumentType;
import com.inf.daycare.models.Role;
import com.inf.daycare.models.User;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    //User -> GetUserDto
    @Mapping(target = "documentType", source = "documentType.name")
    @Mapping(target = "rolesNames", source = "roles")
    GetUserDto userToGetUserDto(User user);

    default List<String> mapRolesToNames(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }

    @Mapping(target = "contactTypeName", source = "contactType.name")
    GetContactDto contactToContactDto(Contact contact);

    List<GetContactDto> contactListToContactDtoList(List<Contact> contacts);

    //List<User> -> List<GetUserDto>
    List<GetUserDto> userListToGetUserDtoList(List<User> users);

    // Método para convertir de DocumentType a String
    default String mapDocumentTypeToString(DocumentType documentType) {
        return documentType != null ? documentType.getName() : null;
    }

    //Convertir PostUserDto to PostTutorDto
    PostTutorDto getPostUserDtotoToPostTutorDto(PostUserDto userDto);

    //Convertir PostUserDto to Director
    PostDirectorDto getPostUserDtotoToPostDirectorDto(PostUserDto userDto);

    //Convertir PostUserDto to Teacher
    @Mapping(target = "dni", source = "document")
    PostTeacherDto getPostUserDtotoToPostTeacherDto(PostUserDto userDto);
}
