package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.get.GetCommentDto;
import com.inf.daycare.dtos.get.GetCommentRateDto;
import com.inf.daycare.dtos.post.PostCommentDto;
import com.inf.daycare.dtos.put.PutCommentDto;
import com.inf.daycare.exceptions.CommentLimitException;
import com.inf.daycare.mapper.CommentMapper;
import com.inf.daycare.models.Comment;
import com.inf.daycare.models.Daycare;
import com.inf.daycare.models.Tutor;
import com.inf.daycare.repositories.CommentRepository;
import com.inf.daycare.services.CommentService;
import com.inf.daycare.services.DaycareService;
import com.inf.daycare.services.TutorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TutorService tutorService;
    private final CommentMapper commentMapper;
    private final DaycareService daycareService;

    @Value("${app.comment.daily-limit:3}") // Límite diario por defecto de 3 comentarios
    private int dailyCommentLimit;

    @Override
    public GetCommentDto getById(Long commentId) {
        Comment comment = getCommentOrThrow(commentId);

        return commentMapper.commentToGetCommentDto(comment);
    }

    @Override
    public List<GetCommentDto> getAllByParentId(Long parentId) {
        List<Comment> comments = commentRepository.findAllByTutor_Id(parentId)
                .orElse(Collections.emptyList());

        return commentMapper.commentListToGetCommentDtoList(comments);
    }

    @Override
    public GetCommentRateDto getAllByDaycareId(Long daycareId) {
        //Obtener todos los comentarios de la guardería en orden descendente por fecha de creación
        List<Comment> comments = commentRepository.findByDaycareIdOrderByCreatedAtDesc(daycareId)
                .orElse(Collections.emptyList());

        //Mensaje de reseñas
        String rating = getRateCommentForDaycare(comments);

        return commentMapper.commentToGetCommentRateDto(rating, comments);
    }

    @Override
    @Transactional
    public GetCommentDto create(PostCommentDto postCommentDto, Long tutorId) {
        //Verificar si la guardería y el tutor existen
        Daycare daycare = daycareService.getDaycareOrThrow(postCommentDto.getDaycareId());
        Tutor tutor = tutorService.getTutorOrThrow(tutorId);

        //Definir el rango de tiempo para el día actual
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        //Verificar el límite de comentarios del tutor para esta guardería hoy
        long existingCommentsCount = commentRepository.countByTutor_IdAndDaycare_IdAndCreatedAtBetween(
                tutor.getId(),
                daycare.getId(),
                startOfDay,
                endOfDay
        );

        //Si el número de comentarios existentes es igual o mayor al límite diario, lanzar una excepción
        if (existingCommentsCount >= dailyCommentLimit) {
            throw new CommentLimitException("El tutor ya ha excedido el límite de " + dailyCommentLimit + " comentarios para esta guardería hoy.");
        }

        //Mapeo de DTO a entidad y seteo manual de las relaciones
        Comment comment = commentMapper.postCommentDtoToComment(postCommentDto);
        comment.setDaycare(daycare);
        comment.setTutor(tutor);

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

    @Override
    public void changeStatus(Long commentId, Boolean status) {
        Comment comment = getCommentOrThrow(commentId);
        comment.setEnable(status);

        commentRepository.save(comment);
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
