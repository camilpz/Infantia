package com.inf.daycare.repositories;

import com.inf.daycare.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepostitory extends JpaRepository<Role, Long> {
}
