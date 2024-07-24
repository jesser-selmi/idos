package com.IDOSdigital.userManagement.repositories;

import com.IDOSdigital.userManagement.entities.Request;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.IDOSdigital.userManagement.repositories.customised.CustomizedRequestRepository;

@Repository
public interface RequestRepository extends MongoRepository<Request, String>, CustomizedRequestRepository {

}
