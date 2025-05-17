package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.get.GetDaycareDto;
import com.inf.daycare.dtos.post.PostDaycareDto;
import com.inf.daycare.dtos.put.PutDaycareDto;
import com.inf.daycare.mapper.DaycareMapper;
import com.inf.daycare.models.Daycare;
import com.inf.daycare.repositories.DaycareRepository;
import com.inf.daycare.repositories.DirectorRepository;
import com.inf.daycare.services.DaycareService;
import com.inf.daycare.services.DirectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DaycareServiceImpl implements DaycareService {

    private final DaycareRepository daycareRepository;
    private final DirectorServiceImpl directorService;
    private final DaycareMapper daycareMapper;


    @Override
    public GetDaycareDto getById(Long daycareId) {
        Daycare daycare = getDaycareOrThrow(daycareId);

        return daycareMapper.daycareToGetDaycareDto(daycare);
    }

    @Override
    public List<GetDaycareDto> getAllDaycaresByDirectorId(Long directorId) {
        //Verificar si existe el director
        directorService.getDirectorOrThrow(directorId);

        List<Daycare> daycares = daycareRepository.findAllByDirectorId(directorId)
                .orElse(Collections.emptyList());

        return daycareMapper.daycaresToGetDaycareDtos(daycares);
    }

    @Override
    public GetDaycareDto create(PostDaycareDto postDaycareDto) {
        Daycare daycare = daycareMapper.postDaycareDtoToDaycare(postDaycareDto);

        daycareRepository.save(daycare);

        return daycareMapper.daycareToGetDaycareDto(daycare);
    }

    @Override
    public GetDaycareDto update(Long daycareId, PutDaycareDto putDaycareDto) {
        Daycare daycare = getDaycareOrThrow(daycareId);
        daycareMapper.updateDaycareFromPutDaycareDto(putDaycareDto, daycare);

        daycareRepository.save(daycare);

        return daycareMapper.daycareToGetDaycareDto(daycare);
    }

    @Override
    public void disable(Long daycareId) {
        Daycare daycare = getDaycareOrThrow(daycareId);
        daycare.setEnabled(false);

        daycareRepository.save(daycare);
    }

    //-----------------------------------------------------Métodos auxiliares-----------------------------------------------------
    private Daycare getDaycareOrThrow(Long daycareId) {
        return daycareRepository.findById(daycareId)
                .orElseThrow(() -> new RuntimeException("Daycare not found"));
    }
}
