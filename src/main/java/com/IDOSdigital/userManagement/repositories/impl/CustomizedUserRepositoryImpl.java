package com.IDOSdigital.userManagement.repositories.impl;

import com.IDOSdigital.userManagement.entities.User;
import com.IDOSdigital.userManagement.repositories.customised.CustomizedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CustomizedUserRepositoryImpl implements CustomizedUserRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Optional<User> findUserByEmail(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        User user = mongoTemplate.findOne(query, User.class);
        return Optional.ofNullable(user);
    }
}
