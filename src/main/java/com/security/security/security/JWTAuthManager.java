package com.security.security.security;

import com.security.security.entities.UserEntity;
import com.security.security.services.JwtService;
import com.security.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JWTAuthManager implements AuthenticationManager {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;

    @Override
    public JWTAuthenticationFilter.JWTAuthentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof JWTAuthenticationFilter.JWTAuthentication)) {
            throw new IllegalStateException("This Authentication Manager only deals with JWT Authentication");
        }
        JWTAuthenticationFilter.JWTAuthentication jwtAuth = (JWTAuthenticationFilter.JWTAuthentication) authentication;
        String username = jwtService.decodeJwt(jwtAuth.getCredentials());
        UserEntity userEntity = userService.findUserByUsername(username);
        jwtAuth.setUserEntity(userEntity);
        return jwtAuth;
    }
}
