package com.inf.daycare.controllers;

import com.inf.daycare.exceptions.CommentLimitException;
import com.inf.daycare.exceptions.InvalidScheduleException;
import com.inf.daycare.exceptions.RelationAlreadyExistsException;
import com.inf.daycare.exceptions.ScheduleConflictException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RelationAlreadyExistsException.class)
    public ResponseEntity<String> handleRelationAlreadyExistsException(RelationAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ScheduleConflictException.class)
    public ResponseEntity<String> handleScheduleConflictException(ScheduleConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidScheduleException.class)
    public ResponseEntity<String> handleInvalidScheduleException(InvalidScheduleException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(CommentLimitException.class)
    public ResponseEntity<String> handleCommentLimitException(CommentLimitException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
