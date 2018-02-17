package org.souhaib.caremy.api.module.service;

import org.souhaib.caremy.api.module.model.User;
import org.souhaib.caremy.api.module.repository.UserRepository;
import org.souhaib.caremy.security.module.model.Authority;
import org.souhaib.caremy.security.module.model.UserRequest;
import org.souhaib.caremy.security.module.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private AuthorityService authService;

    @Override
    public User save(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setFirstName(userRequest.getFirstname());
        user.setLastName(userRequest.getLastname());
        List<Authority> auth = authService.findByName("ROLE_USER");
        user.setAuthorities(auth);
        this.userRepository.save(user);
        return user;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
