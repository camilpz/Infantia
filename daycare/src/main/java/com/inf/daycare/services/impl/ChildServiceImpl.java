package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.get.GetAuthorizedPersonDto;
import com.inf.daycare.dtos.get.GetChildDto;
import com.inf.daycare.dtos.post.PostAuthorizedPersonDto;
import com.inf.daycare.dtos.post.PostChildDto;
import com.inf.daycare.dtos.put.PutChildDto;
import com.inf.daycare.exceptions.RelationAlreadyExistsException;
import com.inf.daycare.mapper.ChildMapper;
import com.inf.daycare.models.*;
import com.inf.daycare.repositories.AuthorizedPersonRepository;
import com.inf.daycare.repositories.ChildRepository;
import com.inf.daycare.repositories.TutorChildRepository;
import com.inf.daycare.repositories.TutorRepository;
import com.inf.daycare.services.AuthorizedPersonService;
import com.inf.daycare.services.ChildService;
import com.inf.daycare.services.TutorService;
import jakarta.persistence.EntityNotFoundException;
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
    private final AuthorizedPersonService authorizedPersonService;
    private final AuthorizedPersonRepository authorizedPersonRepository;

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
    @Transactional // Asegúrate de que este método sea transaccional
    public GetChildDto create(PostChildDto postChildDto, Long tutorId) {
        Tutor tutor = tutorService.getTutorOrThrow(tutorId);

        Child child;

        //Verificar si el niño ya está registrado por DNI, Fecha de Nacimiento y Nombres
        Optional<Child> existingChild = childRepository.findByDniAndBirthDateAndFirstNameAndLastName(
                postChildDto.getDni(),
                postChildDto.getBirthDate(),
                postChildDto.getFirstName(),
                postChildDto.getLastName());

        // Si ya existe, se usa el existente; si no, se guarda el nuevo
        if(existingChild.isPresent()) {
            child = existingChild.get();
            // Si el niño ya existe, es importante que la colección de AuthorizedPeople esté cargada
            // si se va a modificar (lazy loading).
            // Si no está cargada automáticamente (por `@Transactional` o fetch type EAGER),
            // podrías necesitar `child.getAuthorizedPeople().size();` o similar para forzar la carga.
        } else {
            child = childMapper.postChildDtoToChild(postChildDto);
            child = childRepository.save(child);
        }

        //Verificar y crear la relación Tutor-Niño
        boolean relationExists = tutorChildRepository.existsByTutorIdAndChildId(tutor.getId(), child.getId());

        if (!relationExists) {
            TutorChild tutorChild = new TutorChild(tutor, child);
            // Si TutorChild usa @EmbeddedId o @IdClass, la forma comentada sería más explícita:
            // TutorChild tutorChild = TutorChild.builder()
            //         .tutor(tutor)
            //         .child(child)
            //         .id(new TutorChildId(tutor.getId(), child.getId())) // Asegúrate de que TutorChildId y el constructor existan
            //         .build();

            // Guardar la relación entre el tutor y el niño
            tutorChildRepository.save(tutorChild);

            User user = tutor.getUser();

            //Validar que el tutor tenga un usuario asociado
            if (user == null) {
                throw new IllegalStateException("El tutor no tiene un usuario asociado para ser persona autorizada.");
            }

            // Obtener número de teléfono del usuario (de sus contactos)
            String phoneNumber = user.getContacts().stream()
                    .filter(contact -> {
                        String type = contact.getContactType().getName();
                        return type.equals("CELULAR") || type.equals("TELEFONO_FIJO");
                    })
                    .map(Contact::getContent)
                    .findFirst()
                    .orElse(null);

            //Crear el DTO para la persona autorizada
            PostAuthorizedPersonDto postAuthorizedPersonDto = new PostAuthorizedPersonDto();
            postAuthorizedPersonDto.setFirstName(tutor.getFirstName());
            postAuthorizedPersonDto.setLastName(tutor.getLastName());
            postAuthorizedPersonDto.setDni(user.getDocument());
            postAuthorizedPersonDto.setRelationshipToChild(tutor.getRelationshipToChild());
            postAuthorizedPersonDto.setPhoneNumber(phoneNumber);
            postAuthorizedPersonDto.setEmail(user.getEmail());

            //VER SI YA EXISTE UNA PERSONA AUTORIZADA CON EL MISMO DNI
            GetAuthorizedPersonDto getAuthorizedPersonDto;
            try {
                //Intentar buscar por DNI para reutilizar una persona autorizada existente
                getAuthorizedPersonDto = authorizedPersonService.getAuthorizedPersonByDni(postAuthorizedPersonDto.getDni());
            } catch (EntityNotFoundException e) {
                //Si no se encuentra, crear una nueva persona autorizada
                getAuthorizedPersonDto = authorizedPersonService.createAuthorizedPerson(postAuthorizedPersonDto);
            }

            // Obtener la entidad AuthorizedPerson
            AuthorizedPerson authorizedPersonToAssociate = authorizedPersonRepository.findById(getAuthorizedPersonDto.getId())
                    .orElseThrow(() -> new IllegalStateException("Error interno: AuthorizedPerson no encontrada después de crear/obtener por DNI.")); // Esto no debería ocurrir

            //Añadir la persona autorizada al niño
            child.getAuthorizedPeople().add(authorizedPersonToAssociate);

            childRepository.save(child);

        } else {
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
