package org.souhaib.caremy.api.module.service;

import org.souhaib.caremy.api.module.model.User;

import java.util.List;


public interface UserService {

    User findById(Long id);

    User findByUsername(String username);

    List<User> findAll();
}
