package com.IDOSdigital.userManagement.controllers;

import com.IDOSdigital.userManagement.entities.User;
import com.IDOSdigital.userManagement.services.UserService;
import com.IDOSdigital.userManagement.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @QueryMapping
    @Secured({"ROLE_ADMIN", "ROLE_RH"})
    public Response getAllUsers() {
        return userService.getAllUsers();
    }

    @QueryMapping
    public Response getUserById(@Argument String id) {
        return userService.getUserById(id);
    }

    @MutationMapping
    @Secured("ROLE_ADMIN")
    public Response createUser(@Argument User user) {
        return userService.createUser(user);
    }

    @MutationMapping
    public Response updateUser(@Argument User user, @Argument String id) {
        return userService.updateUser(user, id);
    }

    @MutationMapping
    @Secured("ROLE_ADMIN")
    public Response deleteUser(@Argument String id) {
        return userService.deleteUser(id);
    }
}
