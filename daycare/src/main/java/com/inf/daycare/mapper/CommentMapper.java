package com.inf.daycare.mapper;

import com.inf.daycare.dtos.get.GetCommentDto;
import com.inf.daycare.dtos.get.GetCommentRateDto;
import com.inf.daycare.dtos.post.PostCommentDto;
import com.inf.daycare.dtos.put.PutCommentDto;
import com.inf.daycare.models.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    //Comment -> GetCommentDto
    GetCommentDto commentToGetCommentDto(Comment comment);

    //List<Comment> -> List<GetCommentDto>
    List<GetCommentDto> commentListToGetCommentDtoList(List<Comment> comments);

   //Método personalizado para mapear Comment y lista de comentarios a GetCommentRateDto
   default GetCommentRateDto commentToGetCommentRateDto(String rating, List<Comment> comments) {
       GetCommentRateDto dto = new GetCommentRateDto();
       dto.setRate(rating);
       dto.setComments(commentListToGetCommentDtoList(comments));
       return dto;
   }

    //PostCommentDto -> Comment
    Comment postCommentDtoToComment(PostCommentDto postCommentDto);

    //Update Comment from PutCommentDto
    void updateCommentFromPutCommentDto(PutCommentDto putCommentDto, @MappingTarget Comment comment);
}
