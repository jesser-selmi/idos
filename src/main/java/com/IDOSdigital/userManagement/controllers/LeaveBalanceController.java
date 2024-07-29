package com.IDOSdigital.userManagement.controllers;

import com.IDOSdigital.userManagement.entities.LeaveBalance;
import com.IDOSdigital.userManagement.services.LeaveBalanceService;
import com.IDOSdigital.userManagement.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;


@Controller
public class LeaveBalanceController {

    @Autowired
    private LeaveBalanceService leaveBalanceService;

    @QueryMapping
    @Secured({"ROLE_ADMIN", "ROLE_RH"})
    public Response getAllLeaveBalances() {
        return leaveBalanceService.getAllLeaveBalances();
    }

    @QueryMapping
    @Secured({"ROLE_ADMIN", "ROLE_RH"})
    public Response getLeaveBalanceById(@Argument String id) {
        return leaveBalanceService.getLeaveBalanceById(id);
    }

    @MutationMapping
    @Secured({"ROLE_ADMIN", "ROLE_RH"})
    public Response createLeaveBalance(@Argument LeaveBalance leaveBalance) {
        return leaveBalanceService.createLeaveBalance(leaveBalance);
    }

    @MutationMapping
    @Secured({"ROLE_ADMIN", "ROLE_RH"})
    public Response updateLeaveBalance(@Argument LeaveBalance leaveBalance , @Argument String id) {
        return leaveBalanceService.updateLeaveBalance(leaveBalance , id);
    }

    @MutationMapping
    @Secured({"ROLE_ADMIN", "ROLE_RH"})
    public Response deleteLeaveBalance(@Argument String id) {
        return leaveBalanceService.deleteLeaveBalance(id);
    }
}
