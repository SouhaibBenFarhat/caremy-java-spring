package org.souhaib.caremy.security.module.rest;

import org.souhaib.caremy.api.module.model.User;
import org.souhaib.caremy.api.module.service.UserService;
import org.souhaib.caremy.security.module.common.DeviceProvider;
import org.souhaib.caremy.security.module.model.UserRequest;
import org.souhaib.caremy.security.module.model.UserTokenState;
import org.souhaib.caremy.security.module.security.TokenHelper;
import org.souhaib.caremy.security.module.security.auth.JwtAuthenticationRequest;
import org.souhaib.caremy.security.module.service.CustomUserDetailsService;
import org.souhaib.caremy.utility.module.exception.ResourceConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {


    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private DeviceProvider deviceProvider;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, Device device) throws AuthenticationException, IOException {


    try {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        String jws = tokenHelper.generateToken(user.getUsername(), device);
        int expiresIn = tokenHelper.getExpiredIn(device);
        return ResponseEntity.ok(new UserTokenState(jws, expiresIn));
    }catch (BadCredentialsException e){
        return ResponseEntity.notFound().build();
    }


    }



    @RequestMapping(method = RequestMethod.POST, value = "/signup")
    public ResponseEntity<?> addUser(@RequestBody UserRequest userRequest,
                                     UriComponentsBuilder ucBuilder) {

        User existUser = this.userService.findByUsername(userRequest.getUsername());
        if (existUser != null) {
            throw new ResourceConflictException(userRequest.getId(), "Username already exists");
        }
        User user = this.userService.save(userRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/user/{userId}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }




    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public ResponseEntity<?> refreshAuthenticationToken(
            HttpServletRequest request,
            HttpServletResponse response,
            Principal principal
    ) {

        String authToken = tokenHelper.getToken(request);

        Device device = deviceProvider.getCurrentDevice(request);

        if (authToken != null && principal != null) {

            // TODO check user password last update
            String refreshedToken = tokenHelper.refreshToken(authToken, device);
            int expiresIn = tokenHelper.getExpiredIn(device);

            return ResponseEntity.ok(new UserTokenState(refreshedToken, expiresIn));
        } else {
            UserTokenState userTokenState = new UserTokenState();
            return ResponseEntity.accepted().body(userTokenState);
        }
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChanger passwordChanger) {
        userDetailsService.changePassword(passwordChanger.oldPassword, passwordChanger.newPassword);
        Map<String, String> result = new HashMap<>();
        result.put("result", "success");
        return ResponseEntity.accepted().body(result);
    }


    @GetMapping("/whoami")
    @PreAuthorize("hasRole('USER')")
    public User user(Principal user) {
        return this.userService.findByUsername(user.getName());
    }


    static class PasswordChanger {
        public String oldPassword;
        public String newPassword;
    }
}
