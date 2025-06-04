package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.get.GetChildDto;
import com.inf.daycare.dtos.post.PostChildDto;
import com.inf.daycare.dtos.put.PutChildDto;
import com.inf.daycare.exceptions.RelationAlreadyExistsException;
import com.inf.daycare.mapper.ChildMapper;
import com.inf.daycare.models.Child;
import com.inf.daycare.models.Tutor;
import com.inf.daycare.models.TutorChild;
import com.inf.daycare.models.TutorChildId;
import com.inf.daycare.repositories.ChildRepository;
import com.inf.daycare.repositories.TutorChildRepository;
import com.inf.daycare.repositories.TutorRepository;
import com.inf.daycare.services.ChildService;
import com.inf.daycare.services.TutorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {

    private final ChildRepository childRepository;
    private final TutorService tutorService;
    private final TutorChildRepository tutorChildRepository;
    private final ChildMapper childMapper;

    @Override
    public GetChildDto getById(Long childId) {
        Child child = getChildOrThrow(childId);

        return childMapper.getChildToGetChildDto(child);
    }

    @Override
    public List<GetChildDto> getAllChildren() {
        List<Child> children = childRepository.findAll();

        return childMapper.childListToGetChildDtoList(children);
    }

    @Override
    public List<GetChildDto> getAllChildrenByStatus(Boolean enabled) {
        //Busca los niños según su estado (habilitado o deshabilitado)
        List<Child> children = childRepository.findAllByEnabled(enabled);

        return childMapper.childListToGetChildDtoList(children);
    }

    @Override
    public GetChildDto create(PostChildDto postChildDto, Long tutorId) {
        Tutor tutor = tutorService.getTutorOrThrow(tutorId);

        Child child = childMapper.postChildDtoToChild(postChildDto);

        //Verificar si el niño ya está registrado
        Optional<Child> existingChild = childRepository.findByDniAndBirthDateAndFirstNameAndLastName
                (postChildDto.getDni(),
                        postChildDto.getBirthDate(),
                        postChildDto.getFirstName(),
                        postChildDto.getLastName());

        //Si ya existe, se usa el existente, si no, se guarda el nuevo
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
    @Transactional
    public GetChildDto disableChild(Long childId, Long tutorId) {
        //Ver si el tutor existe y tiene una relación con el niño
        TutorChild tutorChild = getTutorChildOrThrow(new TutorChildId(tutorId, childId));

        //Obtener el niño y deshabilitarlo
        Child child = getChildOrThrow(childId);
        child.setEnabled(false);

        Child disabledChild = childRepository.save(child);

        return childMapper.getChildToGetChildDto(disabledChild);
    }

    /*@Override
    public GetChildDto deleteChild(Long childId, Long tutorId) {
        //Obtener el tutor y el niño
        Tutor tutor = tutorService.getTutorOrThrow(tutorId);
        Child child = getChildOrThrow(childId);

        TutorChildId tutorChildId = new TutorChildId(tutor.getId(), child.getId());

        //Borrar la relación entre este tutor y el niño
        tutorChildRepository.deleteById(tutorChildId);

        //Verificar si el niño tiene más tutores asociados
        boolean hasOtherTutors = tutorChildRepository.existsByChildId(childId);

        //Si no tiene más tutores, se elimina el niño
        if (!hasOtherTutors) childRepository.delete(child);

        return childMapper.getChildToGetChildDto(child);
    }*/

    //------------------------------------------------Métodos auxiliares----------------------------------------------------

    //Por ahora no tiene enabled
    public Child getChildOrThrow(Long id) {
        return childRepository.findById(id).orElseThrow(() -> new RuntimeException("Niño no encontrado"));
    }

    public TutorChild getTutorChildOrThrow(TutorChildId id) {
        return tutorChildRepository.findById(id).orElseThrow(() -> new RuntimeException("No existe una relación entre el tutor y el niño"));
    }

    @Override
    public Integer calculateChildAge(Child child) {
        // Asegúrate de que el campo de fecha de nacimiento en Child se llame 'birthDate'
        if (child.getBirthDate() == null) {
            throw new IllegalArgumentException("La fecha de nacimiento del niño no puede ser nula para calcular la edad.");
        }
        return Period.between(child.getBirthDate(), LocalDate.now()).getYears();
    }

    @Override
    public boolean isChildAssociatedWithTutor(Long childId, Long tutorId) {
        // Usa el repositorio de TutorChild para verificar la existencia de la relación
        return tutorChildRepository.existsByTutorIdAndChildId(tutorId, childId);
    }
}
