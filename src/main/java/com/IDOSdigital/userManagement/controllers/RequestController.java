package com.IDOSdigital.userManagement.controllers;

import com.IDOSdigital.userManagement.entities.Request;
import com.IDOSdigital.userManagement.services.RequestService;
import com.IDOSdigital.userManagement.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

@Controller
public class RequestController {

    @Autowired
    private RequestService requestService;

    @QueryMapping
    @Secured({"ROLE_ADMIN", "ROLE_RH"})
    public Response getAllRequests() {
        return requestService.getAllRequests();
    }

    @QueryMapping
    @Secured({"ROLE_USER", "ROLE_INTERN"})
    public Response getAllUserRequests() {
        return requestService.getAllUserRequests();
    }

    @QueryMapping
    @Secured({"ROLE_ADMIN", "ROLE_RH"})
    public Response getRequestById(@Argument String id) {
        return requestService.getRequestById(id);
    }

    @MutationMapping
    @Secured({"ROLE_USER", "ROLE_INTERN"})
    public Response createRequest(@Argument Request request) {
        return requestService.createRequest(request);
    }

    @MutationMapping
    @Secured({"ROLE_ADMIN", "ROLE_RH"})
    public Response updateRequest(@Argument Request request, @Argument String id) {
        return requestService.updateRequest(request , id);
    }
}
