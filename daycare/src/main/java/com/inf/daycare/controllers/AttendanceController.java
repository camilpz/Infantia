package com.inf.daycare.controllers;

import com.inf.daycare.dtos.get.GetAttendanceDto;
import com.inf.daycare.dtos.put.PutAttendanceDto;
import com.inf.daycare.enums.ShiftEnum;
import com.inf.daycare.services.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("/getAllByChildId/{childId}")
    public ResponseEntity<List<GetAttendanceDto>> getAllByChildId(@PathVariable Long childId) {
        List<GetAttendanceDto> getAttendanceDtos = attendanceService.getAllByChildId(childId);
        return ResponseEntity.ok(getAttendanceDtos);
    }

    @GetMapping("/getAllByDaycare/{daycareId}/Classroom/{classroomId}")
    public ResponseEntity<List<GetAttendanceDto>> getAllByDaycareAndClassroom(@PathVariable Long daycareId, @PathVariable Long classroomId) {
        List<GetAttendanceDto> getAttendanceDtos = attendanceService.getAllByDaycareAndClassroom(daycareId, classroomId);
        return ResponseEntity.ok(getAttendanceDtos);
    }

    @PostMapping("/createAttendanceList/{daycareId}/{classroomId}/{shift}")
    public ResponseEntity<List<GetAttendanceDto>> createListofAttendanceforToday(@PathVariable Long daycareId, @PathVariable Long classroomId, @PathVariable ShiftEnum shift) {
        List<GetAttendanceDto> getAttendanceDtos = attendanceService.createListofAttendanceforToday(daycareId, classroomId, shift);
        return ResponseEntity.ok(getAttendanceDtos);
    }

    @PutMapping("/update")
    public ResponseEntity<List<GetAttendanceDto>> updateAttendanceForToday(@RequestBody List<PutAttendanceDto> putAttendanceDtos) {
        List<GetAttendanceDto> getAttendanceDtos = attendanceService.updateAttendanceForToday(putAttendanceDtos);
        return ResponseEntity.ok(getAttendanceDtos);
    }

}
