package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.get.GetPickupDto;
import com.inf.daycare.dtos.post.PostPickupDto;
import com.inf.daycare.mapper.PickupMapper;
import com.inf.daycare.models.Attendance;
import com.inf.daycare.models.AuthorizedPerson;
import com.inf.daycare.models.Child;
import com.inf.daycare.models.Pickup;
import com.inf.daycare.repositories.AttendanceRepository;
import com.inf.daycare.repositories.AuthorizedPersonRepository;
import com.inf.daycare.repositories.ChildRepository;
import com.inf.daycare.repositories.PickupRepository;
import com.inf.daycare.services.PickupService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PickupServiceImpl implements PickupService {

    private final PickupRepository pickUpRepository;
    private final ChildRepository childRepository;
    private final AuthorizedPersonRepository authorizedPersonRepository;
    private final AttendanceRepository attendanceRepository; // Necesario para actualizar checkOutTime
    private final PickupMapper pickupRecordMapper;

    @Override
    @Transactional
    public GetPickupDto recordPickup(PostPickupDto postPickupRecordDto) {
        //Comprobar que existan los IDs necesarios
        Child child = childRepository.findById(postPickupRecordDto.getChildId())
                .orElseThrow(() -> new EntityNotFoundException("Niño no encontrado con ID: " + postPickupRecordDto.getChildId()));

        AuthorizedPerson authorizedPerson = authorizedPersonRepository.findById(postPickupRecordDto.getAuthorizedPersonId())
                .orElseThrow(() -> new EntityNotFoundException("Persona autorizada no encontrada con ID: " + postPickupRecordDto.getAuthorizedPersonId()));

        Attendance attendance = attendanceRepository.findById(postPickupRecordDto.getAttendanceId())
                .orElseThrow(() -> new EntityNotFoundException("Asistencia no encontrada con ID: " + postPickupRecordDto.getAttendanceId()));

        //Verificar si la persona autorizada realmente está autorizada para este niño
        if (!child.getAuthorizedPeople().contains(authorizedPerson)) {
            // Esto requiere que la lista authorizedPeople en Child esté correctamente cargada (eager o fetch)
            // Si es lazy, necesitarás cargarla explícitamente o asegurar que JPA lo haga.
            // Una forma más segura es hacer una query específica en el repositorio si la colección es grande.
            // Por ahora, asumimos que el Set está cargado.
            throw new IllegalArgumentException("La persona con ID " + authorizedPerson.getId() +
                    " no está autorizada para recoger al niño con ID " + child.getId());
        }

        if (attendance.getCheckOutTime() != null || pickUpRepository.findByAttendance_Id(attendance.getId()).isPresent()) {
            throw new IllegalStateException("Esta asistencia ya tiene un registro de salida o un retiro asociado.");
        }
        // Puedes añadir validación de fecha si quieres asegurarte de que el retiro sea para la asistencia de hoy
        if (!attendance.getAttendanceDate().isEqual(LocalDate.now())) {
            // O simplemente una advertencia, depende de tu lógica de negocio
            // throw new IllegalArgumentException("El registro de asistencia no corresponde a la fecha actual.");
        }


        Pickup pickup = pickupRecordMapper.postPickupDtoToPickup(postPickupRecordDto);

        pickup.setChild(child);
        pickup.setPickedUpBy(authorizedPerson);
        pickup.setAttendance(attendance);


        //Actualizar el checkOutTime en la entidad Attendance utilizando el tiempo del retiro
        attendance.setCheckOutTime(pickup.getPickupTime());

        //Guardar ambas entidades
        pickup = pickUpRepository.save(pickup);
        attendanceRepository.save(attendance);

        return pickupRecordMapper.pickupToGetPickupDto(pickup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetPickupDto> getAllPickupRecords() {
        List<Pickup> pickupRecords = pickUpRepository.findAll();
        return pickupRecordMapper.listPickupToGetPickupDtoList(pickupRecords);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetPickupDto> getPickupRecordsByChildId(Long childId) {
        List<Pickup> pickupRecords = pickUpRepository.findByChild_Id(childId);
        return pickupRecordMapper.listPickupToGetPickupDtoList(pickupRecords);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetPickupDto> getPickupRecordsByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX); // O date.plusDays(1).atStartOfDay()
        List<Pickup> pickupRecords = pickUpRepository.findByPickupTimeBetween(startOfDay, endOfDay);
        return pickupRecordMapper.listPickupToGetPickupDtoList(pickupRecords);
    }

    @Override
    @Transactional(readOnly = true)
    public GetPickupDto getPickupRecordById(Long id) {
        Pickup pickupRecord = getPickupOrThrow(id);
        return pickupRecordMapper.pickupToGetPickupDto(pickupRecord);
    }

    //---------------------------------------------------Métodos auxiliares---------------------------------------------------

    public Pickup getPickupOrThrow(Long id) {
        return pickUpRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro de retiro no encontrado con ID: " + id));
    }
}
