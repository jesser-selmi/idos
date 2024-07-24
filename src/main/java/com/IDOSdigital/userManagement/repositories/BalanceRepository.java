package com.IDOSdigital.userManagement.repositories;

import com.IDOSdigital.userManagement.entities.Balance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.IDOSdigital.userManagement.repositories.customised.CustomizedBalanceRepository;

@Repository
public interface BalanceRepository extends MongoRepository<Balance, String>, CustomizedBalanceRepository {

}
