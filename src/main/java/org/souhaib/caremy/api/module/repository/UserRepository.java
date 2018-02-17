package org.souhaib.caremy.api.module.repository;

import org.souhaib.caremy.api.module.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUsername(String username);
}
