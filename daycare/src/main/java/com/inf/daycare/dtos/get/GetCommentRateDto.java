package com.inf.daycare.dtos.get;

import lombok.Data;

import java.util.List;

@Data
public class GetCommentRateDto {
    private String rate;
    private List<GetCommentDto> comments;
}
