package com.inf.daycare.services;

import com.inf.daycare.dtos.get.GetActivityDto;
import com.inf.daycare.dtos.post.PostActivityDto;
import com.inf.daycare.dtos.put.PutActivityDto;
import com.inf.daycare.models.Activity;

import java.util.List;

public interface ActivityService {
    GetActivityDto getActivityById(Long activityId);
    List<GetActivityDto> getAllActivitiesByClassroomId(Long classroomId);
    GetActivityDto createActivity(PostActivityDto postActivityDto);
    GetActivityDto updateActivity(Long activityId, PutActivityDto putActivityDto);
    void deleteActivity(Long activityId);
    Activity getActivityOrThrow(Long activityId);
}
