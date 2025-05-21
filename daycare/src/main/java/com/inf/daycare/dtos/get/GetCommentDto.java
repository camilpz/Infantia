package com.inf.daycare.dtos.get;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetCommentDto {
    private Long id;
    private Long parentId;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
