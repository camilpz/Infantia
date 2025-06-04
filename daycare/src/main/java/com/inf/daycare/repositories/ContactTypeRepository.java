package com.inf.daycare.repositories;

import com.inf.daycare.models.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactTypeRepository extends JpaRepository<ContactType, Long> {
    List<ContactType> findAllByName(String name);
}
