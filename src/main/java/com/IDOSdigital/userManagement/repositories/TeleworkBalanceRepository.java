package com.IDOSdigital.userManagement.repositories;

import com.IDOSdigital.userManagement.entities.TeleworkBalance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.IDOSdigital.userManagement.repositories.customised.CustomizedTeleworkBalanceRepository;


@Repository
public interface TeleworkBalanceRepository extends MongoRepository<TeleworkBalance, String>, CustomizedTeleworkBalanceRepository {

}
