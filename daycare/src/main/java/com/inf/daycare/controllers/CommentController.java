package com.inf.daycare.controllers;

import com.inf.daycare.dtos.get.GetCommentDto;
import com.inf.daycare.dtos.get.GetCommentRateDto;
import com.inf.daycare.dtos.post.PostCommentDto;
import com.inf.daycare.dtos.put.PutCommentDto;
import com.inf.daycare.services.CommentService;
import com.inf.daycare.services.impl.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final AuthService authService;

    private Long getCurrentTutorId() {
        return authService.getLoggedInTutorId();
    }

    @GetMapping("/getById/{commentId}")
    public ResponseEntity<GetCommentDto> getCommentById(@PathVariable Long commentId) {
        GetCommentDto getCommentDto = commentService.getById(commentId);

        return ResponseEntity.ok(getCommentDto);
    }

    @GetMapping("/getAllByParentId")
    public ResponseEntity<List<GetCommentDto>> getAllCommentsByParentId() {
        List<GetCommentDto> comments = commentService.getAllByParentId(getCurrentTutorId());

        return ResponseEntity.ok(comments);
    }

    @GetMapping("/getAllByDaycareId/{daycareId}")
    public ResponseEntity<GetCommentRateDto> getAllCommentsByDaycareId(@PathVariable Long daycareId) {
        GetCommentRateDto comments = commentService.getAllByDaycareId(daycareId);

        return ResponseEntity.ok(comments);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('TUTOR')")
    public ResponseEntity<GetCommentDto> createComment(@RequestBody PostCommentDto postCommentDto) {
        GetCommentDto createdComment = commentService.create(postCommentDto, getCurrentTutorId());

        return ResponseEntity.ok(createdComment);
    }

    @PutMapping("/edit/{commentId}")
    public ResponseEntity<GetCommentDto> editComment(@PathVariable Long commentId, @RequestBody PutCommentDto putCommentDto) {
        GetCommentDto updatedComment = commentService.edit(commentId, putCommentDto);

        return ResponseEntity.ok(updatedComment);
    }

    /*@DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.noContent().build();
    }*/

    @PutMapping("/changeStatus/{commentId}")
    public ResponseEntity<Void> changeCommentStatus(@PathVariable Long commentId, @RequestParam Boolean status) {
        commentService.changeStatus(commentId, status);
        return ResponseEntity.noContent().build();
    }
}
