package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.get.GetDirectorDto;
import com.inf.daycare.dtos.post.PostDirectorDto;
import com.inf.daycare.dtos.put.PutDirectorDto;
import com.inf.daycare.mapper.DirectorMapper;
import com.inf.daycare.models.Director;
import com.inf.daycare.models.Title;
import com.inf.daycare.models.User;
import com.inf.daycare.repositories.DirectorRepository;
import com.inf.daycare.repositories.TitleRepository;
import com.inf.daycare.services.DirectorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {

    private final DirectorRepository directorRepository;
    private final DirectorMapper directorMapper;
    private final TitleRepository titleRepository;

    @Override
    public GetDirectorDto getById(Long directorId) {
        Director director = getDirectorOrThrow(directorId);

        return directorMapper.directorToGetDirectorDto(director);
    }

    @Override
    public GetDirectorDto getByUserId(Long userId) {
        Director director = directorRepository.findByUser_Id(userId)
                .orElseThrow(() -> new EntityNotFoundException("Director no encontrado con la id de usuario: " + userId));

        return directorMapper.directorToGetDirectorDto(director);
    }

    @Override
    public Director create(PostDirectorDto postDirectorDto, User user) {
        Director director = directorMapper.postDirectorDtoToDirector(postDirectorDto);
        director.setUser(user);
        director.setTitles(postDirectorDto.getTitles());

        Director savedDirector = directorRepository.save(director);

        return savedDirector;
    }

    @Override
    @Transactional
    public GetDirectorDto update(Long directorId, PutDirectorDto putDirectorDto) {
        Director director = getDirectorOrThrow(directorId);
        directorMapper.updateDirectorFromPutDirectorDto(putDirectorDto, director);

       Set<Title> titles = putDirectorDto.getTitleIds().stream()
               .map(this::getTitleOrThrow)
               .toList()
               .stream()
               .collect(Collectors.toSet());

       director.setTitles(titles);

        directorRepository.save(director);

        return directorMapper.directorToGetDirectorDto(director);
    }

    @Override
    public void disable(Long directorId) {
        Director director = getDirectorOrThrow(directorId);
        director.setEnabled(false);

        directorRepository.save(director);
    }

    //----------------------------------------------------------Métodos auxiliares-------------------------------------------------------------

    public Director getDirectorOrThrow(Long directorId) {
        return directorRepository.findById(directorId)
                .orElseThrow(() -> new EntityNotFoundException("Director no encontrado"));
    }

    public Director getDirectorByUserOrThrow(Long userId) {
        return directorRepository.findByUser_Id(userId)
                .orElseThrow(() -> new EntityNotFoundException("Director no encontrado para el usuario: " + userId));
    }

    public Title getTitleOrThrow(Long titleId) {
        return titleRepository.findById(titleId)
                .orElseThrow(() -> new EntityNotFoundException("Título no encontrado con ID: " + titleId));
    }
}
