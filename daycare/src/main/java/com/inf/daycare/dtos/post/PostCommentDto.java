package com.inf.daycare.dtos.post;

import lombok.Data;

@Data
public class PostCommentDto {
    private Long daycareId;
    private Integer rating;
    private String content;
}
