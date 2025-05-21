package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.get.GetDaycareDto;
import com.inf.daycare.dtos.post.PostDaycareDto;
import com.inf.daycare.dtos.put.PutDaycareDto;
import com.inf.daycare.exceptions.RelationAlreadyExistsException;
import com.inf.daycare.mapper.DaycareMapper;
import com.inf.daycare.models.Daycare;
import com.inf.daycare.models.TeacherDaycare;
import com.inf.daycare.repositories.DaycareRepository;
import com.inf.daycare.repositories.DirectorRepository;
import com.inf.daycare.repositories.TeacherDaycareRepository;
import com.inf.daycare.services.DaycareService;
import com.inf.daycare.services.DirectorService;
import com.inf.daycare.services.TeacherService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DaycareServiceImpl implements DaycareService {

    private final DaycareRepository daycareRepository;
    private final DirectorServiceImpl directorService;
    private final TeacherDaycareRepository teacherDaycareRepository;
    private final TeacherService teacherService;
    private final DaycareMapper daycareMapper;


    @Override
    public GetDaycareDto getById(Long daycareId) {
        Daycare daycare = getDaycareOrThrow(daycareId);

        return daycareMapper.daycareToGetDaycareDto(daycare);
    }

    @Override
    public List<GetDaycareDto> getAllDaycares() {
        List<Daycare> daycares = daycareRepository.findAll();

        return daycareMapper.daycaresToGetDaycareDtos(daycares);
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

    @Override
    public Boolean addTeacherToDaycare(Long daycareId, Long teacherId) {
        if(!teacherDaycareRepository.existsByTeacherIdAndDaycareId(teacherId, daycareId)) {
            //Verificar si existe el daycare y el profesor
            TeacherDaycare teacherDaycare = TeacherDaycare.builder()
                    .daycare(getDaycareOrThrow(daycareId))
                    .teacher(teacherService.getTeacherOrThrow(teacherId))
                    .build();

            //Guardar la relación
            teacherDaycareRepository.save(teacherDaycare);
        }
        else throw new RelationAlreadyExistsException("Ya existe una relación entre el tutor y el niño");

        return true;
    }

    //-----------------------------------------------------Métodos auxiliares-----------------------------------------------------//

    public Daycare getDaycareOrThrow(Long daycareId) {
        return daycareRepository.findById(daycareId)
                .orElseThrow(() -> new EntityNotFoundException("Guardería no encontrada"));
    }
}
