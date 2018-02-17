package org.souhaib.caremy.security.module.service;

import org.souhaib.caremy.security.module.model.Authority;
import org.souhaib.caremy.security.module.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorityServiceImplementation implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;


    @Override
    public List<Authority> findById(Long id) {
        Authority auth = this.authorityRepository.findOne(id);
        List<Authority> auths = new ArrayList<>();
        auths.add(auth);
        return auths;
    }

    @Override
    public List<Authority> findByName(String name) {

        Authority auth = this.authorityRepository.findByName(name);
        List<Authority> auths = new ArrayList<>();
        auths.add(auth);
        return auths;

    }
}
