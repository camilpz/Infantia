package com.inf.daycare.repositories;

import com.inf.daycare.models.TutorChild;
import com.inf.daycare.models.TutorChildId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TutorChildRepository extends JpaRepository<TutorChild, TutorChildId> {
    //Buscar todos los niños que tiene un tutor
    List<TutorChild> findByTutorId(Long tutorId);

    //Buscar todos los tutores que tiene un niño
    List<TutorChild> findByChildId(Long childId);

    //Buscar si existe una relación entre un tutor y un niño
    boolean existsByTutorIdAndChildId(Long tutorId, Long childId);

    boolean existsByChildId(Long childId);

    //Buscar todos los niños que tiene un tutor
    List<TutorChild> findAllByTutorId(Long tutorId);

}