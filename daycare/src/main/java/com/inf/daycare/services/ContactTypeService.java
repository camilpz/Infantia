package com.inf.daycare.services;

import com.inf.daycare.dtos.get.GetContactTypeDto;
import jakarta.annotation.Nullable;

import java.util.List;

public interface ContactTypeService {
    List<GetContactTypeDto> getAllContactTypes(@Nullable String name);
}
