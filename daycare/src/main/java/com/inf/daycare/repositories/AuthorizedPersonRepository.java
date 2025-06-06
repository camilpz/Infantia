package com.inf.daycare.repositories;

import com.inf.daycare.models.AuthorizedPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorizedPersonRepository extends JpaRepository<AuthorizedPerson, Long> {
    Optional<AuthorizedPerson> findByDni(String dni);
}
