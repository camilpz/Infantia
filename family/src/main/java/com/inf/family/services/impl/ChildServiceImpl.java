package com.inf.family.services.impl;

import com.inf.family.dtos.get.GetChildDto;
import com.inf.family.dtos.post.PostChildDto;
import com.inf.family.dtos.put.PutChildDto;
import com.inf.family.exceptions.RelationAlreadyExistsException;
import com.inf.family.mapper.ChildMapper;
import com.inf.family.models.Child;
import com.inf.family.models.Tutor;
import com.inf.family.models.TutorChild;
import com.inf.family.models.TutorChildId;
import com.inf.family.repositories.ChildRepository;
import com.inf.family.repositories.TutorChildRepository;
import com.inf.family.repositories.TutorRepository;
import com.inf.family.services.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {

    private final ChildRepository childRepository;
    private final TutorRepository tutorRepository;
    private final TutorChildRepository tutorChildRepository;
    private final ChildMapper childMapper;

    @Override
    public GetChildDto create(PostChildDto postChildDto, Long tutorId) {
        Tutor tutor = getTutorOrThrow(tutorId);

        Child child = childMapper.postChildDtoToChild(postChildDto);

        //Verificar si el niño ya está registrado
        Optional<Child> existingChild = childRepository.findByDni(child.getDni());

        if(existingChild.isPresent()) {
            child = existingChild.get();
        }
        else childRepository.save(child);

        boolean relationExists = tutorChildRepository.existsByTutorIdAndChildId(tutor.getId(), child.getId());

        if (!relationExists) {
            TutorChild tutorChild = TutorChild.builder()
                    .tutor(tutor)
                    .child(child)
                    .id(new TutorChildId(tutor.getId(), child.getId()))
                    .build();
            tutorChildRepository.save(tutorChild);
        }
        else{
            throw new RelationAlreadyExistsException("Ya existe una relación entre el tutor y el niño");
        }

        return childMapper.getChildToGetChildDto(child);
    }

    @Override
    public GetChildDto edit(Long id, PutChildDto putChildDto) {
        Child child = getChildOrThrow(id);

        childMapper.updateChildFromPutChildDto(putChildDto, child);
        childRepository.save(child);

        return childMapper.getChildToGetChildDto(child);
    }

    @Override
    public List<GetChildDto> getAllByTutorId(Long tutorId) {
        List<TutorChild> tutorChild = tutorChildRepository.findByTutorId(tutorId);

        List<Child> children = tutorChild.stream()
                .map(TutorChild::getChild)
                .toList();

        return childMapper.childListToGetChildDtoList(children);
    }

    @Override
    public GetChildDto deleteChild(Long childId, Long tutorId) {
        Tutor tutor = getTutorOrThrow(tutorId);
        Child child = getChildOrThrow(childId);

        TutorChildId tutorChildId = new TutorChildId(tutor.getId(), child.getId());

        //Borrar la relación entre este tutor y el niño
        tutorChildRepository.deleteById(tutorChildId);

        //Verificar si el niño tiene más tutores asociados
        boolean hasOtherTutors = tutorChildRepository.existsByChildId(childId);

        //Si no tiene más tutores, se elimina el niño
        if (!hasOtherTutors) childRepository.delete(child);

        return childMapper.getChildToGetChildDto(child);
    }

    //------------------------------------------------Métodos privados----------------------------------------------------

    private Tutor getTutorOrThrow(Long id) {
        return tutorRepository.findByIdAndEnabledTrue(id).orElseThrow(() -> new RuntimeException("Tutor not found or disabled"));
    }

    //Por ahora no tiene enabled
    private Child getChildOrThrow(Long id) {
        return childRepository.findById(id).orElseThrow(() -> new RuntimeException("Child not found or disabled"));
    }

    private TutorChild getTutorChildOrThrow(TutorChildId id) {
        return tutorChildRepository.findById(id).orElseThrow(() -> new RuntimeException("TutorChild not found or disabled"));
    }
}
