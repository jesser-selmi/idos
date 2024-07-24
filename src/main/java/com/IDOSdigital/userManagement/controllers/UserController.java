package com.IDOSdigital.userManagement.controllers;

import com.IDOSdigital.userManagement.entities.User;
import com.IDOSdigital.userManagement.services.UserService;
import com.IDOSdigital.userManagement.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @QueryMapping
    public Response getAllUsers() {
        return userService.getAllUsers();
    }

    @QueryMapping
    public Response getUserById(@Argument String id) {
        return userService.getUserById(id);
    }

    @MutationMapping
    public Response createUser(@Argument User user) {

        return userService.createUser(user);
    }

    @MutationMapping
    public Response updateUser(@Argument User user, @Argument String id) {

        return userService.updateUser(user, id);
    }

    @MutationMapping
    public Response deleteUser(@Argument String id) {

        return userService.deleteUser(id);
    }
}
