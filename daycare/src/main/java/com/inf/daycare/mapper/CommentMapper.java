// src/main/java/com/inf/daycare/mapper/CommentMapper.java
package com.inf.daycare.mapper;

import com.inf.daycare.dtos.get.GetCommentDto;
import com.inf.daycare.dtos.get.GetCommentRateDto;
import com.inf.daycare.dtos.post.PostCommentDto;
import com.inf.daycare.dtos.put.PutCommentDto;
import com.inf.daycare.models.Comment;
import com.inf.daycare.models.Tutor; // <-- Importa la entidad Tutor
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named; // <-- Necesario para referenciar métodos "named" en @Mapping

import java.util.List;
import java.util.Optional; // Para manejar Optional en el método auxiliar

@Mapper(componentModel = "spring")
public interface CommentMapper {

    // Método auxiliar para obtener el nombre completo del tutor, manejando nulos
    @Named("tutorFullName") // Le damos un nombre para referenciarlo en @Mapping
    default String mapTutorFullName(Comment comment) {
        // Usa Optional para manejar de forma segura los nulos en la cadena de llamadas
        return Optional.ofNullable(comment)
                .map(Comment::getTutor) // Si comment no es null, obtén el tutor
                .map(tutor -> { // Si tutor no es null, combina el nombre y apellido
                    String name = Optional.ofNullable(tutor.getFirstName()).orElse("");
                    String lastName = Optional.ofNullable(tutor.getLastName()).orElse("");
                    // Si ambos son vacíos, podrías devolver "Tutor desconocido" o simplemente un vacío.
                    if (name.isEmpty() && lastName.isEmpty()) {
                        return "Tutor desconocido"; // O null, o ""
                    }
                    return (name + " " + lastName).trim(); // trim() para quitar espacios extra si uno es vacío
                })
                .orElse("Tutor no disponible"); // Si comment o tutor es null
    }


    // Comment -> GetCommentDto
    // Ahora, mapeamos el campo parentFullName al resultado de nuestro método auxiliar
    // @Mapping(target = "parentName", source = "tutor.name") // <-- Eliminar/Modificar
    // @Mapping(target = "parentLastName", source = "tutor.lastName") // <-- Eliminar
    @Mapping(target = "parentFullName", source = "comment", qualifiedByName = "tutorFullName") // <-- ¡Nuevo mapeo!
    GetCommentDto commentToGetCommentDto(Comment comment);

    // List<Comment> -> List<GetCommentDto>
    List<GetCommentDto> commentListToGetCommentDtoList(List<Comment> comments);

    // Método personalizado para mapear Comment y lista de comentarios a GetCommentRateDto
    default GetCommentRateDto commentToGetCommentRateDto(String rating, List<Comment> comments) {
        GetCommentRateDto dto = new GetCommentRateDto();
        dto.setRate(rating);
        dto.setComments(commentListToGetCommentDtoList(comments)); // Este mapeo usa el de arriba que ya tiene el nombre completo
        return dto;
    }

    // PostCommentDto -> Comment
    Comment postCommentDtoToComment(PostCommentDto postCommentDto);

    // Update Comment from PutCommentDto
    void updateCommentFromPutCommentDto(PutCommentDto putCommentDto, @MappingTarget Comment comment);
}