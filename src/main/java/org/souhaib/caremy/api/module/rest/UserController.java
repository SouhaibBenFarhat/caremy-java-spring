package org.souhaib.caremy.api.module.rest;

import org.souhaib.caremy.api.module.model.User;
import org.souhaib.caremy.api.module.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping( value = "/api", produces = MediaType.APPLICATION_JSON_VALUE )
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping( method = GET, value = "/user/{userId}" )
    @PreAuthorize("hasRole('ADMIN')")
    public User loadById(@PathVariable Long userId ) {
        return this.userService.findById( userId );
    }

    @RequestMapping( method = GET, value= "/user/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> loadAll() {
        return this.userService.findAll();
    }




}
