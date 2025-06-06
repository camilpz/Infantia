package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.NearbyDaycareSearchDto;
import com.inf.daycare.dtos.get.GetDaycareDto;
import com.inf.daycare.dtos.post.PostDaycareDto;
import com.inf.daycare.dtos.post.PostDaycareShiftDefinitionDto;
import com.inf.daycare.dtos.put.PutDaycareDto;
import com.inf.daycare.enums.ShiftEnum;
import com.inf.daycare.exceptions.RelationAlreadyExistsException;
import com.inf.daycare.mapper.DaycareMapper;
import com.inf.daycare.mapper.DaycareShiftDefinitionMapper;
import com.inf.daycare.models.*;
import com.inf.daycare.repositories.DaycareRepository;
import com.inf.daycare.repositories.DaycareShiftDefinitionRepository;
import com.inf.daycare.repositories.DirectorRepository;
import com.inf.daycare.repositories.TeacherDaycareRepository;
import com.inf.daycare.services.DaycareService;
import com.inf.daycare.services.DirectorService;
import com.inf.daycare.services.TeacherService;
import com.inf.daycare.utils.DistanceCalculator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DaycareServiceImpl implements DaycareService {

    private final DaycareRepository daycareRepository;
    private final DirectorService directorService;
    private final DaycareShiftDefinitionRepository daycareShiftDefinitionRepository;
    private final TeacherDaycareRepository teacherDaycareRepository;
    private final TeacherService teacherService;
    private final DaycareMapper daycareMapper;
    private final DaycareShiftDefinitionMapper daycareShiftDefinitionMapper;


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

        List<Daycare> daycares = daycareRepository.findAllByDirector_Id(directorId)
                .orElse(Collections.emptyList());

        return daycareMapper.daycaresToGetDaycareDtos(daycares);
    }

    @Override
    @Transactional
    public GetDaycareDto create(PostDaycareDto postDaycareDto, Long directorId) {
        Director director = directorService.getDirectorOrThrow(directorId);
        Daycare daycare = daycareMapper.postDaycareDtoToDaycare(postDaycareDto);
        daycare.setDirector(director);

        daycareRepository.save(daycare);

        return daycareMapper.daycareToGetDaycareDto(daycare);
    }

    @Override
    @Transactional
    public GetDaycareDto update(Long daycareId, PutDaycareDto putDaycareDto) {
        Daycare daycare = getDaycareOrThrow(daycareId);
        daycareMapper.updateDaycareFromPutDaycareDto(putDaycareDto, daycare);

        daycareRepository.save(daycare);

        return daycareMapper.daycareToGetDaycareDto(daycare);
    }

    @Override
    @Transactional
    public void disable(Long daycareId) {
        Daycare daycare = getDaycareOrThrow(daycareId);
        daycare.setEnabled(false);

        daycareRepository.save(daycare);
    }

    @Override
    @Transactional
    public void enable(Long daycareId) {
        Daycare daycare = getDaycareOrThrow(daycareId);
        daycare.setEnabled(true);

        daycareRepository.save(daycare);
    }

    @Override
    @Transactional
    public void validateDaycare(Long daycareId) {
        Daycare daycare = getDaycareOrThrow(daycareId);
        daycare.setValidated(true);

        daycareRepository.save(daycare);
    }

    @Override
    @Transactional
    public void addTeacherToDaycare(Long daycareId, Long teacherId) {
        Teacher teacher = teacherService.getTeacherOrThrow(teacherId);
        Daycare daycare = getDaycareOrThrow(daycareId);

        if(!teacherDaycareRepository.existsByTeacherIdAndDaycareId(teacherId, daycareId)) {
            //Verificar si existe el daycare y el profesor
            TeacherDaycare teacherDaycare = new TeacherDaycare(teacher, daycare);

            //Guardar la relación
            teacherDaycareRepository.save(teacherDaycare);
        }
        else throw new RelationAlreadyExistsException("Ya existe una relación entre el profesor y la guardería");

    }

    @Override
    @Transactional // Esta operación modifica el estado de la base de datos
    public void removeTeacherFromDaycare(Long daycareId, Long teacherId, Long directorId) {
        //Verificar guarderia, director y profesor existan
        Daycare daycare = getDaycareOrThrow(daycareId);

        teacherService.getTeacherOrThrow(teacherId);

        Director director = directorService.getDirectorOrThrow(directorId);

        if (!daycare.getDirector().getId().equals(director.getId())) {
            throw new AccessDeniedException("Acceso denegado. Solo el director de esta guardería puede remover profesores.");
        }

        //Buscar la relación entre el profesor y la guardería
        TeacherDaycare teacherDaycare = teacherDaycareRepository.findByDaycareIdAndTeacherId(daycareId, teacherId)
                .orElseThrow(() -> new EntityNotFoundException("La relación entre el profesor y la guardería no existe."));

        //Eliminar la relación
        teacherDaycareRepository.delete(teacherDaycare);
    }

    @Override
    @Transactional
    public DaycareShiftDefinition createOrUpdateShiftDefinition(Long daycareId, PostDaycareShiftDefinitionDto dto, Long directorId) {
        //Buscar y verificar que la guardería y el director existan
        Daycare daycare = getDaycareOrThrow(daycareId);
        Director director = directorService.getDirectorOrThrow(directorId);

        //Verificar que el director sea el director de la guardería
        if (!daycare.getDirector().getId().equals(director.getId())) {
            throw new AccessDeniedException("Acceso denegado. Solo el director de esta guardería puede crear o actualizar definiciones de turno.");
        }
        return createOrUpdateShiftDefinitionInternal(daycare, dto);
    }

    private DaycareShiftDefinition createOrUpdateShiftDefinitionInternal(Daycare daycare, PostDaycareShiftDefinitionDto dto) {
        Optional<DaycareShiftDefinition> existingDefinition = daycareShiftDefinitionRepository.findByDaycareAndShiftType(daycare, dto.getShiftType());

        DaycareShiftDefinition definition;
        if (existingDefinition.isPresent()) {
            definition = existingDefinition.get();
            //Actualizar la definición de turno existente
            daycareShiftDefinitionMapper.updateEntityFromDto(dto, definition);
        } else {
            definition = daycareShiftDefinitionMapper.postDtoToEntity(dto);
            //Relacionar la guardería con la definición de turno
            definition.setDaycare(daycare);
        }
        return daycareShiftDefinitionRepository.save(definition);
    }

    @Transactional(readOnly = true)
    public List<GetDaycareDto> findNearbyDaycares(NearbyDaycareSearchDto searchDto) {
        //Validación básica de los parámetros de búsqueda
        if (searchDto.getLatitude() == null || searchDto.getLongitude() == null || searchDto.getRadiusKm() == null) {
            throw new IllegalArgumentException("Latitud, longitud y radio son requeridos para la búsqueda.");
        }
        if (searchDto.getRadiusKm() <= 0) {
            throw new IllegalArgumentException("El radio debe ser un valor positivo.");
        }

        List<Daycare> allDaycares = daycareRepository.findAll(); // Obtiene todas las guarderías (o solo las habilitadas)

        //Filtra las guarderías por distancia
        List<Daycare> nearbyDaycares = allDaycares.stream()
                .filter(daycare -> daycare.getLatitude() != null && daycare.getLongitude() != null) // Solo guarderías con coordenadas
                .filter(daycare -> {
                    double distance = DistanceCalculator.calculateDistance( // Usa el utilitario
                            searchDto.getLatitude(),
                            searchDto.getLongitude(),
                            daycare.getLatitude(),
                            daycare.getLongitude()
                    );
                    return distance <= searchDto.getRadiusKm();
                })
                .collect(Collectors.toList());

        return nearbyDaycares.stream()
                .map(daycare -> daycareMapper.daycareToGetDaycareDto(daycare))
                .collect(Collectors.toList());
    }

    //-----------------------------------------------------Métodos auxiliares-----------------------------------------------------//

    public Daycare getDaycareOrThrow(Long daycareId) {
        return daycareRepository.findById(daycareId)
                .orElseThrow(() -> new EntityNotFoundException("Guardería no encontrada"));
    }

    @Override
    public DaycareShiftDefinition getDaycareShiftDefinitionOrThrow(Daycare daycare, ShiftEnum shiftType) {
        return daycareShiftDefinitionRepository.findByDaycareAndShiftType(daycare, shiftType)
                .orElseThrow(() -> new EntityNotFoundException("Definición de turno '" + shiftType + "' no encontrada para la guardería con ID: " + daycare.getId()));
    }
}
