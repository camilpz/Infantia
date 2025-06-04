package com.inf.daycare.repositories;

import com.inf.daycare.models.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {
    Optional<Child> findByDni(String dni);
    //Buscar por dni, fecha de nacimiento, nombre y apellido, si todo esto coincide el niño ya existe
    Optional<Child> findByDniAndBirthDateAndFirstNameAndLastName(
            String dni,
            LocalDate birthDate,
            String firstName,
            String lastName
    );
    List<Child> findAllByEnabled(boolean enabled);
}
