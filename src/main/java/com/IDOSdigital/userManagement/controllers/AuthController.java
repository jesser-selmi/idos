package com.IDOSdigital.userManagement.controllers;

import com.IDOSdigital.userManagement.security.token.TokenProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;

@Controller
public class    AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProcessor tokenProcessor;

    @MutationMapping
    public String login(@Argument String email, @Argument String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            String token = tokenProcessor.generateToken(authentication);
            return token;
        } catch (AuthenticationException e) {
            return "Invalid email or password";
        }
    }
}
