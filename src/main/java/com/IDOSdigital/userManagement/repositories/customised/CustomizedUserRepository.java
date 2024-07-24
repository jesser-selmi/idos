package com.IDOSdigital.userManagement.repositories.customised;

import com.IDOSdigital.userManagement.entities.User;
import java.util.Optional;

public interface CustomizedUserRepository {
    Optional<User> findUserByEmail(String email);
}
