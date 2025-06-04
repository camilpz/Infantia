package com.inf.daycare.services;

import com.inf.daycare.dtos.get.GetDirectorDto;
import com.inf.daycare.dtos.post.PostDirectorDto;
import com.inf.daycare.dtos.put.PutDirectorDto;
import com.inf.daycare.models.Director;
import com.inf.daycare.models.User;

public interface DirectorService {
    GetDirectorDto getById(Long directorId);
    GetDirectorDto getByUserId(Long userId);
    Director create(PostDirectorDto postDirectorDto, User user);
    GetDirectorDto update(Long directorId, PutDirectorDto putDirectorDto);
    void disable(Long directorId);

    Director getDirectorByUserOrThrow(Long userId);
    Director getDirectorOrThrow(Long directorId);
}
