package com.IDOSdigital.userManagement.services;

import com.IDOSdigital.userManagement.entities.User;
import com.IDOSdigital.userManagement.utils.Response;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;


public interface UserService {
    Response getAllUsers();
    Response getUserById(String id);
    Response createUser(User user);
    Response updateUser(User user, String id);
    Response deleteUser(String id);

    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
    Optional<User> findUserByEmail(String email);

}
