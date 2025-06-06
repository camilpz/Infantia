package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.get.GetTitleDto;
import com.inf.daycare.mapper.TitleMapper;
import com.inf.daycare.models.Title;
import com.inf.daycare.repositories.TitleRepository;
import com.inf.daycare.services.TitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TitleServiceImpl implements TitleService {
    private final TitleRepository titleRepository;
    private final TitleMapper titleMapper;

    @Override
    public List<GetTitleDto> getAllTitles() {
        List<Title> titles = titleRepository.findAll();
        return titleMapper.titleListToGetTitleDtoList(titles);
    }
}
