package org.souhaib.caremy.security.module.service;

import org.souhaib.caremy.security.module.model.Authority;

import java.util.List;

public interface AuthorityService {

    List<Authority> findById(Long id);
    List<Authority> findByName(String name);
}
