package com.inf.daycare.repositories;

import com.inf.daycare.models.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //Optional<User> findByEmail(String email);

    @Override
    @EntityGraph(attributePaths = {"roles", "tutorProfile", "teacherProfile", "directorProfile"})
    Optional<User> findById(Long id);

    // Si tienes este método (para UserDetailsServiceImpl)
    // Asegúrate de que solo cargue los roles si es lo que necesitas para el login.
    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByEmail(String email);
}
