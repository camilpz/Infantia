package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.get.GetChildDto;
import com.inf.daycare.dtos.get.GetTutorDto;
import com.inf.daycare.dtos.post.PostTutorDto;
import com.inf.daycare.dtos.put.PutTutorDto;
import com.inf.daycare.mapper.ChildMapper;
import com.inf.daycare.mapper.TutorMapper;
import com.inf.daycare.models.Child;
import com.inf.daycare.models.Tutor;
import com.inf.daycare.models.TutorChild;
import com.inf.daycare.models.User;
import com.inf.daycare.repositories.TutorChildRepository;
import com.inf.daycare.repositories.TutorRepository;
import com.inf.daycare.services.TutorService;
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
    public Tutor create(PostTutorDto postTutorDto, User user) {
        Tutor tutor = tutorMapper.postTutorDtoToTutor(postTutorDto);
        tutor.setUser(user);

        tutorRepository.save(tutor);
        System.out.println("Tutor created, user id is: " + tutor.getUser().getId());

        return tutor;
    }

    @Override
    public GetTutorDto edit(Long tutorId, PutTutorDto putTutorDto) {
        Tutor existingTutor = getTutorOrThrow(tutorId);

        tutorMapper.updateTutorFromPutTutorDto(putTutorDto, existingTutor);

        tutorRepository.save(existingTutor);

        GetTutorDto getTutorDto = tutorMapper.tutorToGetTutorDto(existingTutor);
        getTutorDto.setUserId(existingTutor.getUser().getId());

        return getTutorDto;
    }

    @Override
    public GetTutorDto getTutorById(Long tutorId) {
        Tutor existingTutor = getTutorOrThrow(tutorId);

        GetTutorDto getTutorDto = tutorMapper.tutorToGetTutorDto(existingTutor);
        getTutorDto.setUserId(existingTutor.getUser().getId());

        return getTutorDto;
    }

    @Override
    public GetTutorDto disableTutor(Long tutorId) {
        Tutor existingTutor = getTutorOrThrow(tutorId);

        existingTutor.setEnabled(false);
        tutorRepository.save(existingTutor);

        GetTutorDto getTutorDto = tutorMapper.tutorToGetTutorDto(existingTutor);
        getTutorDto.setUserId(existingTutor.getUser().getId());

        return getTutorDto;
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

    //------------------------------------------------Métodos auxiliares----------------------------------------------------

    public Tutor getTutorOrThrow(Long id) {
        return tutorRepository.findByIdAndEnabledTrue(id).orElseThrow(() -> new RuntimeException("Tutor not found or disabled"));
    }
}
