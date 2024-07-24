package com.IDOSdigital.userManagement.repositories;

import com.IDOSdigital.userManagement.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.IDOSdigital.userManagement.repositories.customised.CustomizedUserRepository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String>, CustomizedUserRepository {
    Optional<User> findUserByEmail(String email);
}
