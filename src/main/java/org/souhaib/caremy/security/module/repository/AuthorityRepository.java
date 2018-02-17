package org.souhaib.caremy.security.module.repository;

import org.souhaib.caremy.security.module.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByName(String name);
}
