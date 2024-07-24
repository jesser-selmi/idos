package com.IDOSdigital.userManagement.repositories;

import com.IDOSdigital.userManagement.entities.LeaveBalance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.IDOSdigital.userManagement.repositories.customised.CustomizedLeaveBalanceRepository;

@Repository
public interface LeaveBalanceRepository extends MongoRepository<LeaveBalance, String>, CustomizedLeaveBalanceRepository {

}
