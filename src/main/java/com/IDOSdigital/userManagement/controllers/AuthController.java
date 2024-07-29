package com.IDOSdigital.userManagement.controllers;

import com.IDOSdigital.userManagement.security.token.TokenProcessor;
import com.IDOSdigital.userManagement.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

@Controller
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProcessor tokenProcessor;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @MutationMapping
    public String login(@Argument String email, @Argument String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            // Make sure TokenProcessor's generateToken method accepts Authentication
            String token = tokenProcessor.generateToken(authentication);
            return token;
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return "Invalid email or password";
        }
    }
}