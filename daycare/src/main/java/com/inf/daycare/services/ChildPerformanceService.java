package com.inf.daycare.services;

import com.inf.daycare.dtos.get.GetChildPerformanceDto;
import com.inf.daycare.dtos.post.PostChildPerformanceDto;
import com.inf.daycare.dtos.put.PutChildPerformaneDto;
import com.inf.daycare.enums.DevelopmentAreaEnum;

import java.time.LocalDate;
import java.util.List;

public interface ChildPerformanceService {
    GetChildPerformanceDto createChildPerformance(PostChildPerformanceDto postChildPerformanceDto);
    GetChildPerformanceDto updateChildPerformance(Long id, PutChildPerformaneDto putChildPerformanceDto);
    void deleteChildPerformance(Long id);
    GetChildPerformanceDto getChildPerformanceById(Long id);
    List<GetChildPerformanceDto> getAllChildPerformances();
    List<GetChildPerformanceDto> getChildPerformancesByChildId(Long childId);
    List<GetChildPerformanceDto> getChildPerformancesByChildIdAndDevelopmentArea(Long childId, DevelopmentAreaEnum area);
    List<GetChildPerformanceDto> getChildPerformancesByTeacherId(Long teacherUserId);
    List<GetChildPerformanceDto> getChildPerformancesByDate(LocalDate date);
}
