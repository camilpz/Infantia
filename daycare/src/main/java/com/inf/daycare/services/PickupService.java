package com.inf.daycare.services;

import com.inf.daycare.dtos.get.GetPickupDto;
import com.inf.daycare.dtos.post.PostPickupDto;

import java.time.LocalDate;
import java.util.List;

public interface PickupService {
    GetPickupDto recordPickup(PostPickupDto postPickupRecordDto);

    List<GetPickupDto> getAllPickupRecords();
    List<GetPickupDto> getPickupRecordsByChildId(Long childId);
    List<GetPickupDto> getPickupRecordsByDate(LocalDate date);
    GetPickupDto getPickupRecordById(Long id);
}
