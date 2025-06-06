package com.inf.daycare.services;

import com.inf.daycare.dtos.get.GetTitleDto;

import java.util.List;

public interface TitleService {
    List<GetTitleDto> getAllTitles();
}
