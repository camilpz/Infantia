package com.inf.daycare.controllers;

import com.inf.daycare.dtos.get.GetDaycareDto;
import com.inf.daycare.dtos.post.PostDaycareDto;
import com.inf.daycare.dtos.post.PostDaycareShiftDefinitionDto;
import com.inf.daycare.dtos.put.PutDaycareDto;
import com.inf.daycare.services.DaycareService;
import com.inf.daycare.services.impl.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/daycare")
@RequiredArgsConstructor
public class DaycareController {
    private final DaycareService daycareService;
    private final AuthService authService;

    private Long getCurrentDirectorId() {
        return authService.getLoggedInDirectorId();
    }

    @GetMapping("/getById/{daycareId}")
    public ResponseEntity<GetDaycareDto> getById(@PathVariable Long daycareId) {
        GetDaycareDto daycare = daycareService.getById(daycareId);
        return ResponseEntity.ok(daycare);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<GetDaycareDto>> getAll() {
        List<GetDaycareDto> daycares = daycareService.getAllDaycares();
        return ResponseEntity.ok(daycares);
    }

    @GetMapping("/getAllByDirectorId")
    public ResponseEntity<List<GetDaycareDto>> getAllByDirectorId() {
        List<GetDaycareDto> daycares = daycareService.getAllDaycaresByDirectorId(getCurrentDirectorId());
        return ResponseEntity.ok(daycares);
    }

    //VER COMO PASAR EL DIRECTOR ID
    @PostMapping("/create")
    @PreAuthorize("hasRole('DIRECTOR')")
    public ResponseEntity<GetDaycareDto> create(@RequestBody PostDaycareDto postDaycareDto) {
        GetDaycareDto createdDaycare = daycareService.create(postDaycareDto, getCurrentDirectorId());
        return ResponseEntity.ok(createdDaycare);
    }

    @PutMapping("/update/{daycareId}")
    public ResponseEntity<GetDaycareDto> update(@PathVariable Long daycareId, @RequestBody PutDaycareDto putDaycareDto) {
        GetDaycareDto updatedDaycare = daycareService.update(daycareId, putDaycareDto);
        return ResponseEntity.ok(updatedDaycare);
    }

    @PostMapping("/assign/{daycareId}/teachers/{teacherId}")
    @PreAuthorize("hasRole('DIRECTOR')") //Solo un director puede asignar profesores
    public ResponseEntity<Boolean> assignTeacherToDaycare(
            @PathVariable Long daycareId,
            @PathVariable Long teacherId) {
        daycareService.addTeacherToDaycare(daycareId, teacherId);
        return ResponseEntity.noContent().build();
    }

    //Desvincular un profesor de una guardería
    @DeleteMapping("/{daycareId}/teachers/{teacherId}")
    @PreAuthorize("hasRole('DIRECTOR')")
    public ResponseEntity<Void> removeTeacherFromDaycare(
            @PathVariable Long daycareId,
            @PathVariable Long teacherId) {
        // Aquí necesitarías un método en DaycareService.removeTeacherFromDaycare()
        // que elimine la relación TeacherDaycare del repositorio
        daycareService.removeTeacherFromDaycare(daycareId, teacherId, getCurrentDirectorId());
        return ResponseEntity.noContent().build();
    }

    //Revisar
    @PutMapping("/disable/{daycareId}")
    public ResponseEntity<Void> disable(@PathVariable Long daycareId) {
        daycareService.disable(daycareId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/enable/{daycareId}")
    public ResponseEntity<Void> enable(@PathVariable Long daycareId) {
        daycareService.enable(daycareId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/validate/{daycareId}")
    //@PreAuthorize("hasRole('ADMIN')") //Solo un administrador puede validar guarderías
    public ResponseEntity<Void> validateDaycare(@PathVariable Long daycareId) {
        daycareService.validateDaycare(daycareId);
        return ResponseEntity.noContent().build();
    }

    // Este endpoint permite a un director configurar o actualizar los horarios específicos de sus turnos.
    @PostMapping("/{daycareId}/shift-definitions")
    @PreAuthorize("hasRole('DIRECTOR')")
    public ResponseEntity<Void> createOrUpdateShiftDefinition(
            @PathVariable Long daycareId,
            @Valid @RequestBody PostDaycareShiftDefinitionDto postDaycareShiftDefinitionDto) {

        daycareService.createOrUpdateShiftDefinition(daycareId, postDaycareShiftDefinitionDto, getCurrentDirectorId());
        return ResponseEntity.noContent().build();
    }
}
