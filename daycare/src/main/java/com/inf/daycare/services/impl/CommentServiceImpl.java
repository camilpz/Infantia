package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.get.GetCommentDto;
import com.inf.daycare.dtos.get.GetCommentRateDto;
import com.inf.daycare.dtos.post.PostCommentDto;
import com.inf.daycare.dtos.put.PutCommentDto;
import com.inf.daycare.mapper.CommentMapper;
import com.inf.daycare.models.Comment;
import com.inf.daycare.models.Daycare;
import com.inf.daycare.repositories.CommentRepository;
import com.inf.daycare.services.CommentService;
import com.inf.daycare.services.DaycareService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final DaycareService daycareService;

    @Override
    public GetCommentDto getById(Long commentId) {
        Comment comment = getCommentOrThrow(commentId);

        return commentMapper.commentToGetCommentDto(comment);
    }

    @Override
    public List<GetCommentDto> getAllByParentId(Long parentId) {
        List<Comment> comments = commentRepository.findAllByParentId(parentId)
                .orElse(Collections.emptyList());

        return commentMapper.commentListToGetCommentDtoList(comments);
    }

    @Override
    public GetCommentRateDto getAllByDaycareId(Long daycareId) {
        //Obtener todos los comentarios de la guardería en orden descendente por fecha de creación
        List<Comment> comments = commentRepository.findAllByDaycare_idOrderByCreatedAtDesc(daycareId)
                .orElse(Collections.emptyList());

        //Mensaje de reseñas
        String rating = getRateCommentForDaycare(comments);

        return commentMapper.commentToGetCommentRateDto(rating, comments);
    }

    @Override
    public GetCommentDto create(PostCommentDto postCommentDto) {
        //Verificar si la guardería existe
        Daycare daycare = daycareService.getDaycareOrThrow(postCommentDto.getDaycareId());

        Comment comment = commentMapper.postCommentDtoToComment(postCommentDto);
        comment.setDaycare(daycare);

        commentRepository.save(comment);

        return commentMapper.commentToGetCommentDto(comment);
    }

    @Override
    public GetCommentDto edit(Long commentId, PutCommentDto putCommentDto) {
        //Obtener el comentario
        Comment comment = getCommentOrThrow(commentId);

        //Actualizar el comentario
        commentMapper.updateCommentFromPutCommentDto(putCommentDto, comment);
        commentRepository.save(comment);

        return commentMapper.commentToGetCommentDto(comment);
    }

    @Override
    public void delete(Long commentId) {
        Comment comment = getCommentOrThrow(commentId);
        commentRepository.delete(comment);
    }

    //-------------------------------------Métodos auxiliares-------------------------------------//

    public Comment getCommentOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comentario no encontrado"));
    }

    //Obtiene el porcentaje de reseñas positivas y devuelve un mensaje según el porcentaje
    public String getRateCommentForDaycare(List<Comment> comments) {
        if(comments.isEmpty()) {
            return "Sin reseñas aún";
        }

        int totalComments = comments.size();
        long positiveComments = comments.stream()
                .filter(comment -> comment.getRating() >= 4)
                .count();

        double positivePercentage = (double) positiveComments / totalComments;
        if (positivePercentage >= 0.80) {
            return "Reseñas muy positivas";
        } else if (positivePercentage >= 0.60) {
            return "Reseñas positivas";
        } else {
            return "Reseñas mixtas o negativas";
        }
    }
}
