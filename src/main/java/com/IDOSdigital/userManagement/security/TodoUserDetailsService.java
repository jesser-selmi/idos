package com.IDOSdigital.userManagement.security;

import com.IDOSdigital.userManagement.entities.User;
import com.IDOSdigital.userManagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TodoUserDetailsService implements UserDetailsService {
    private final UserService userService;
    @Autowired
    public TodoUserDetailsService(UserService userService) {
        this.userService = userService;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new CustomUserDetails(
                user.getUsername(),
                user.getPassword(),
                user.getId(), // Include the user ID
                user.getAuthorities()
        );
    }
}
