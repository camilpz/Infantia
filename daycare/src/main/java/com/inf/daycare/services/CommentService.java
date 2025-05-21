package com.inf.daycare.services;

import com.inf.daycare.dtos.get.GetCommentDto;
import com.inf.daycare.dtos.get.GetCommentRateDto;
import com.inf.daycare.dtos.post.PostCommentDto;
import com.inf.daycare.dtos.put.PutCommentDto;

import java.util.List;

public interface CommentService {
    GetCommentDto getById(Long commentId);
    List<GetCommentDto> getAllByParentId(Long parentId);
    GetCommentRateDto getAllByDaycareId(Long daycareId);
    GetCommentDto create(PostCommentDto postCommentDto);
    GetCommentDto edit(Long commentId, PutCommentDto putCommentDto);

    void delete(Long commentId);
}
