package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.get.GetChildPerformanceDto;
import com.inf.daycare.dtos.post.PostChildPerformanceDto;
import com.inf.daycare.dtos.put.PutChildPerformaneDto;
import com.inf.daycare.enums.DevelopmentAreaEnum;
import com.inf.daycare.mapper.ChildPerformanceMapper;
import com.inf.daycare.models.Child;
import com.inf.daycare.models.ChildPerformance;
import com.inf.daycare.models.User;
import com.inf.daycare.repositories.ChildPerformanceRepository;
import com.inf.daycare.services.ChildPerformanceService;
import com.inf.daycare.services.ChildService;
import com.inf.daycare.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChildPerformanceServiceImpl implements ChildPerformanceService {
    private final ChildPerformanceRepository childPerformanceRepository;
    private final ChildService childService;
    private final UserService userService; // Necesario para encontrar al docente (User)
    private final ChildPerformanceMapper childPerformanceMapper;

    @Override
    @Transactional
    public GetChildPerformanceDto createChildPerformance(PostChildPerformanceDto postChildPerformanceDto) {
        // 1. Buscar entidades relacionadas
        Child child = childService.getChildOrThrow(postChildPerformanceDto.getChildId());
        User teacher = userService.getUserOrThrow(postChildPerformanceDto.getTeacherUserId());
        // TODO: Opcional: Validar que el 'teacher' tenga el rol de 'TEACHER' o 'ADMIN'

        // 2. Mapear DTO a entidad
        ChildPerformance childPerformance = childPerformanceMapper.postChildPerformanceDtoToChildPerformance(postChildPerformanceDto);

        // 3. Establecer relaciones manualmente
        childPerformance.setChild(child);
        childPerformance.setTeacher(teacher);

        // 4. Guardar la entidad
        childPerformance = childPerformanceRepository.save(childPerformance);

        // 5. Mapear y retornar
        return childPerformanceMapper.childPerformanceToGetChildPerformanceDto(childPerformance);
    }

    @Override
    @Transactional
    public GetChildPerformanceDto updateChildPerformance(Long id, PutChildPerformaneDto putChildPerformanceDto) {
        ChildPerformance existingPerformance = childPerformanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro de desempeño no encontrado con ID: " + id));

        // Mapear campos actualizables desde el DTO al objeto existente
        childPerformanceMapper.updateChildPerformanceFromPutDto(putChildPerformanceDto, existingPerformance);

        ChildPerformance updatedPerformance = childPerformanceRepository.save(existingPerformance);
        return childPerformanceMapper.childPerformanceToGetChildPerformanceDto(updatedPerformance);
    }

    @Override
    @Transactional
    public void deleteChildPerformance(Long id) {
        if (!childPerformanceRepository.existsById(id)) {
            throw new EntityNotFoundException("Registro de desempeño no encontrado con ID: " + id);
        }
        childPerformanceRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public GetChildPerformanceDto getChildPerformanceById(Long id) {
        ChildPerformance childPerformance = childPerformanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro de desempeño no encontrado con ID: " + id));
        return childPerformanceMapper.childPerformanceToGetChildPerformanceDto(childPerformance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetChildPerformanceDto> getAllChildPerformances() {
        List<ChildPerformance> performances = childPerformanceRepository.findAll();
        return childPerformanceMapper.listChildPerformanceToGetChildPerformanceDtoList(performances);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetChildPerformanceDto> getChildPerformancesByChildId(Long childId) {
        List<ChildPerformance> performances = childPerformanceRepository.findByChild_IdOrderByEvaluationDateDesc(childId);
        return childPerformanceMapper.listChildPerformanceToGetChildPerformanceDtoList(performances);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetChildPerformanceDto> getChildPerformancesByChildIdAndDevelopmentArea(Long childId, DevelopmentAreaEnum area) {
        List<ChildPerformance> performances = childPerformanceRepository.findByChild_IdAndDevelopmentAreaOrderByEvaluationDateDesc(childId, area);
        return childPerformanceMapper.listChildPerformanceToGetChildPerformanceDtoList(performances);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetChildPerformanceDto> getChildPerformancesByTeacherId(Long teacherUserId) {
        List<ChildPerformance> performances = childPerformanceRepository.findByTeacher_IdOrderByEvaluationDateDesc(teacherUserId);
        return childPerformanceMapper.listChildPerformanceToGetChildPerformanceDtoList(performances);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetChildPerformanceDto> getChildPerformancesByDate(LocalDate date) {
        List<ChildPerformance> performances = childPerformanceRepository.findByEvaluationDate(date);
        return childPerformanceMapper.listChildPerformanceToGetChildPerformanceDtoList(performances);
    }
}
