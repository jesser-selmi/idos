package com.IDOSdigital.userManagement.controllers;

import com.IDOSdigital.userManagement.security.token.TokenProcessor;
import com.IDOSdigital.userManagement.utils.EntityResponse;
import com.IDOSdigital.userManagement.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;

@Controller
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenProcessor tokenProcessor;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, TokenProcessor tokenProcessor) {
        this.authenticationManager = authenticationManager;
        this.tokenProcessor = tokenProcessor;
    }


    @MutationMapping
    public Response login(@Argument String email, @Argument String password) {
        Response response = new Response();
        EntityResponse entityResponse = new EntityResponse();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            String token = tokenProcessor.generateToken(authentication);
            response.setData(token);
            entityResponse.setStatus(200);
            entityResponse.setMessage("Authentication Successful");
            response.setEntityResponse(entityResponse);
            return response;
        } catch (AuthenticationException e) {
            entityResponse.setStatus(403);
            entityResponse.setMessage("Authentication Failed");
            response.setEntityResponse(entityResponse);
            return response;
        }
    }
}