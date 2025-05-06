package com.inf.family.services.impl;

import com.inf.family.dtos.get.GetChildDto;
import com.inf.family.dtos.post.PostTutorDto;
import com.inf.family.dtos.get.GetTutorDto;
import com.inf.family.mapper.ChildMapper;
import com.inf.family.mapper.TutorMapper;
import com.inf.family.models.Child;
import com.inf.family.models.Tutor;
import com.inf.family.models.TutorChild;
import com.inf.family.repositories.ChildRepository;
import com.inf.family.repositories.TutorChildRepository;
import com.inf.family.repositories.TutorRepository;
import com.inf.family.services.TutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TutorServiceImpl implements TutorService {

    private final TutorRepository tutorRepository;
    private final TutorChildRepository tutorChildRepository;
    private final TutorMapper tutorMapper;
    private final ChildMapper childMapper;

    @Override
    public GetTutorDto create(PostTutorDto postTutorDto) {
        Tutor tutor = tutorMapper.postTutorDtoToTutor(postTutorDto);

        tutorRepository.save(tutor);

        return tutorMapper.tutorToGetTutorDto(tutor);
    }

    @Override
    public GetTutorDto edit(Long tutorId, PostTutorDto postTutorDto) {
        Tutor existingTutor = getTutorOrThrow(tutorId);

        tutorMapper.updateTutorFromPostTutorDto(postTutorDto, existingTutor);

        tutorRepository.save(existingTutor);

        return tutorMapper.tutorToGetTutorDto(existingTutor);
    }

    @Override
    public GetTutorDto getTutorById(Long tutorId) {
        Tutor existingTutor = getTutorOrThrow(tutorId);

        return tutorMapper.tutorToGetTutorDto(existingTutor);
    }

    @Override
    public GetTutorDto deleteTutor(Long tutorId) {
        Tutor existingTutor = getTutorOrThrow(tutorId);

        existingTutor.setEnabled(false);
        tutorRepository.save(existingTutor);

        return tutorMapper.tutorToGetTutorDto(existingTutor);
    }

    @Override
    public List<GetChildDto> getAllChildrensByTutorId(Long tutorId) {
        List<TutorChild> tutorChildren = tutorChildRepository.findAllByTutorId(tutorId);

        List<Child> children = tutorChildren.stream()
                .map(TutorChild::getChild)
                .toList();

        return children.stream()
                .map(childMapper::getChildToGetChildDto)
                .toList();
    }

    //------------------------------------------------Métodos privados----------------------------------------------------

    private Tutor getTutorOrThrow(Long id) {
        return tutorRepository.findByIdAndEnabledTrue(id).orElseThrow(() -> new RuntimeException("Tutor not found or disabled"));
    }
}
