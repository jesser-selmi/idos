package com.IDOSdigital.userManagement.services;

import com.IDOSdigital.userManagement.entities.LeaveBalance;
import com.IDOSdigital.userManagement.utils.Response;


public interface LeaveBalanceService {
    Response getAllLeaveBalances();
    Response getLeaveBalanceById(String id);
    Response createLeaveBalance(LeaveBalance leaveBalance);
    Response updateLeaveBalance(LeaveBalance leaveBalance , String id);
    Response deleteLeaveBalance(String id);
}
