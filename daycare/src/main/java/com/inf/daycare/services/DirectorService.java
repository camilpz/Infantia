package com.inf.daycare.services;

import com.inf.daycare.dtos.get.GetDirectorDto;
import com.inf.daycare.dtos.post.PostDirectorDto;
import com.inf.daycare.dtos.put.PutDirectorDto;

public interface DirectorService {
    GetDirectorDto getById(Long directorId);
    GetDirectorDto getByUserId(Long userId);
    GetDirectorDto create(PostDirectorDto postDirectorDto);
    GetDirectorDto update(Long directorId, PutDirectorDto putDirectorDto);
    void disable(Long directorId);
}
