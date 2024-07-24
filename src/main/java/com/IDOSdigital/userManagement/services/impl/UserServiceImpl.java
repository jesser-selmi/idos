package com.IDOSdigital.userManagement.services.impl;

import com.IDOSdigital.userManagement.entities.User;
import com.IDOSdigital.userManagement.repositories.UserRepository;
import com.IDOSdigital.userManagement.services.UserService;
import com.IDOSdigital.userManagement.utils.EntityResponse;
import com.IDOSdigital.userManagement.utils.ErrorResponse;
import com.IDOSdigital.userManagement.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Response getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        List<User> users = new ArrayList<>();

        for (User user : allUsers) {
            if (!user.isDeleted()) {
                users.add(user);
            }
        }
        Response response = new Response();
        EntityResponse entityResponse = new EntityResponse(200, "Users found");
        response.setEntityResponse(entityResponse);
        response.setData(users);
        return response;
    }

    @Override
    public Response getUserById(String id) {
        Response res = new Response();
        EntityResponse response;
        List<ErrorResponse.ValidationError> errors = new ArrayList<>();
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                res.setData(optionalUser.get());
                response = new EntityResponse(HttpStatus.OK.value(), "User found");
            } else {
                response = new EntityResponse(HttpStatus.NOT_FOUND.value(), "User not found");
            }
        } catch (final Exception e) {
            response = new EntityResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
            errors.add(new ErrorResponse.ValidationError("exception", e.getMessage()));
            response.setErrors(errors);
        }
        res.setEntityResponse(response);
        return res;
    }

    @Override
    public Response createUser(User user) {
        Response res = new Response();
        EntityResponse response;
        List<ErrorResponse.ValidationError> errors = new ArrayList<>();
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User createdUser = userRepository.save(user);
            res.setData(createdUser);
            response = new EntityResponse(HttpStatus.CREATED.value(), "User created successfully");
        } catch (final Exception e) {
            response = new EntityResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
            errors.add(new ErrorResponse.ValidationError("exception", e.getMessage()));
            response.setErrors(errors);
        }
        res.setEntityResponse(response);
        return res;
    }

    @Override
    public Response updateUser(User user, String id) {
        Response res = new Response();
        EntityResponse response;
        List<ErrorResponse.ValidationError> errors = new ArrayList<>();

        try {
            Optional<User> optionalUser = userRepository.findById(id);

            if (optionalUser.isPresent()) {
                User existingUser = optionalUser.get();

                // Update only the attributes from newUser that are not null
                if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
                    existingUser.setFirstName(user.getFirstName());
                }
                if (user.getLastName() != null && !user.getLastName().isEmpty()) {
                    existingUser.setLastName(user.getLastName());
                }
                if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                    existingUser.setEmail(user.getEmail());
                }
                if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                    existingUser.setPassword(user.getPassword());
                }
                if (user.getRole() != null ) {
                    existingUser.setRole(user.getRole());
                }
                if (user.getProfileDescription() != null && !user.getProfileDescription().isEmpty()) {
                    existingUser.setProfileDescription(user.getProfileDescription());
                }
                if (user.getTeleworkBalance() != 0 ) {
                    existingUser.setTeleworkBalance(user.getTeleworkBalance());
                }
                if (user.getLeaveBalance() != 0) {
                    existingUser.setLeaveBalance(user.getLeaveBalance());
                }

                // Save the updated user
                User updatedUser = userRepository.save(existingUser);

                res.setData(updatedUser);
                response = new EntityResponse(HttpStatus.OK.value(), "User updated successfully");
            } else {
                response = new EntityResponse(HttpStatus.NOT_FOUND.value(), "User not found");
            }
        } catch (Exception e) {
            response = new EntityResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
            errors.add(new ErrorResponse.ValidationError("exception", e.getMessage()));
            response.setErrors(errors);
        }

        res.setEntityResponse(response);
        return res;
    }

    @Override
    public Response deleteUser(String id) {
        Response res = new Response();
        EntityResponse response;
        List<ErrorResponse.ValidationError> errors = new ArrayList<>();

        try {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                User existingUser = optionalUser.get();
                existingUser.setDeleted(true); // Soft delete by setting deleted to true
                User updatedUser = userRepository.save(existingUser);
                res.setData(updatedUser);
                response = new EntityResponse(HttpStatus.OK.value(), "User deleted successfully");
            } else {
                response = new EntityResponse(HttpStatus.NOT_FOUND.value(), "User not found");
            }
        } catch (final Exception e) {
            response = new EntityResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
            errors.add(new ErrorResponse.ValidationError("exception", e.getMessage()));
            response.setErrors(errors);
        }

        res.setEntityResponse(response);
        return res;
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());
    }
}
