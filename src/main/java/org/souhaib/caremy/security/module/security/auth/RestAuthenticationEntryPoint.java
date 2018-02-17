package org.souhaib.caremy.security.module.security.auth;

import org.souhaib.caremy.security.module.security.TokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    TokenHelper tokenHelper;

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        if (tokenHelper.isTokenExpired(tokenHelper.getToken(request))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Session Expired");
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        }


    }
}
