package com.inf.daycare.dtos.post;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostCommentDto {
    private Long parentId;
    private Long daycareId;
    private Integer rating;
    private String content;
}
