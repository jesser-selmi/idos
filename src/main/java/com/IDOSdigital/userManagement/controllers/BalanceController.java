package com.IDOSdigital.userManagement.controllers;

import com.IDOSdigital.userManagement.entities.Balance;
import com.IDOSdigital.userManagement.services.BalanceService;
import com.IDOSdigital.userManagement.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

@Controller
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @QueryMapping
    @Secured({"ROLE_ADMIN", "ROLE_RH"})
    public Response getAllBalances() {
        return balanceService.getAllBalances();
    }

    @QueryMapping
    @Secured({"ROLE_ADMIN", "ROLE_RH"})
    public Response getBalanceById(@Argument String id) {
        return balanceService.getBalanceById(id);
    }

    @MutationMapping
    @Secured({"ROLE_ADMIN", "ROLE_RH"})
    public Response createBalance(@Argument Balance balance) {
        return balanceService.createBalance(balance);
    }

    @MutationMapping
    @Secured({"ROLE_ADMIN", "ROLE_RH"})
    public Response updateBalance(@Argument Balance balance , @Argument String id) {
        return balanceService.updateBalance(balance , id);
    }

    @MutationMapping
    @Secured({"ROLE_ADMIN", "ROLE_RH"})
    public Response deleteBalance(@Argument String id) {
        return balanceService.deleteBalance(id);
    }
}
