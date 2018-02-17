package org.souhaib.caremy.api.module.service;

import org.souhaib.caremy.api.module.model.User;
import org.souhaib.caremy.security.module.model.UserRequest;

import java.util.List;


public interface UserService {

    User save(UserRequest userRequest);
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll();
}
