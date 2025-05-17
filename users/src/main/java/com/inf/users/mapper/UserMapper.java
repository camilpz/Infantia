package com.inf.users.mapper;

import com.inf.users.clients.requests.PostTutorRequest;
import com.inf.users.clients.requests.PutTutorRequest;
import com.inf.users.dtos.get.GetUserDto;
import com.inf.users.dtos.post.PostUserTutorDto;
import com.inf.users.dtos.put.PutUserTutorDto;
import com.inf.users.models.DocumentType;
import com.inf.users.models.Role;
import com.inf.users.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    //GetUserDto -> User

    User getUserDtoToUser(GetUserDto userDto);

    //PostUserDto -> User
    User postUserDtoToUser(User userDto);

    //PutUserDto -> User
    User putUserDtoToUser(User userDto);

    //User -> GetUserDto
    @Mapping(target = "rolesNames", source = "roles")
    GetUserDto userToGetUserDto(User user);

    default List<String> mapRolesToNames(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }

    //List<User> -> List<GetUserDto>
    List<GetUserDto> userListToGetUserDtoList(List<User> users);


    //postUserTutorDto -> PostTutorRequest
    PostTutorRequest postUserDtoToPostTutorRequest(PostUserTutorDto postUserTutorDto);

    //PutUserTutorDto -> PutTutorRequest
    PutTutorRequest putUserDtoToPutTutorRequest(PutUserTutorDto putUserTutorDto);

    // Método para convertir de String a DocumentType
    default DocumentType mapStringToDocumentType(String value) {
        if (value == null) {
            return null;
        }
        DocumentType documentType = new DocumentType();
        documentType.setName(value);
        return documentType;
    }

    // Método para convertir de DocumentType a String
    default String mapDocumentTypeToString(DocumentType documentType) {
        return documentType != null ? documentType.getName() : null;
    }
}
