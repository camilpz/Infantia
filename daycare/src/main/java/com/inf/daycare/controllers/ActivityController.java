package com.inf.daycare.controllers;

import com.inf.daycare.dtos.get.GetActivityDto;
import com.inf.daycare.dtos.post.PostActivityDto;
import com.inf.daycare.dtos.put.PutActivityDto;
import com.inf.daycare.services.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping("/getById/{activityId}")
    public ResponseEntity<GetActivityDto> getActivityById(@PathVariable Long activityId) {
        GetActivityDto activity = activityService.getActivityById(activityId);
        return ResponseEntity.ok(activity);
    }

    @GetMapping("/getAllByClassroomId/{classroomId}")
    public ResponseEntity<List<GetActivityDto>> getAllActivitiesByClassroomId(@PathVariable Long classroomId) {
        List<GetActivityDto> activities = activityService.getAllActivitiesByClassroomId(classroomId);
        return ResponseEntity.ok(activities);
    }

    @PostMapping("/create")
    public ResponseEntity<GetActivityDto> createActivity(@RequestBody PostActivityDto postActivityDto) {
        GetActivityDto createdActivity = activityService.createActivity(postActivityDto);
        return ResponseEntity.status(201).body(createdActivity);
    }

    @PutMapping("/update/{activityId}")
    public ResponseEntity<GetActivityDto> updateActivity(@PathVariable Long activityId, @RequestBody PutActivityDto putActivityDto) {
        GetActivityDto updatedActivity = activityService.updateActivity(activityId, putActivityDto);
        return ResponseEntity.ok(updatedActivity);
    }

    @DeleteMapping("/delete/{activityId}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long activityId) {
        activityService.deleteActivity(activityId);
        return ResponseEntity.noContent().build();
    }
}
