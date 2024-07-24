package com.IDOSdigital.userManagement.controllers;

import com.IDOSdigital.userManagement.entities.TeleworkBalance;
import com.IDOSdigital.userManagement.services.TeleworkBalanceService;
import com.IDOSdigital.userManagement.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
@Controller
public class TeleworkBalanceController {

    @Autowired
    private TeleworkBalanceService teleworkBalanceService;

    @QueryMapping
    public Response getAllTeleworkBalances() {
        return teleworkBalanceService.getAllTeleworkBalances();
    }

    @QueryMapping
    public Response getTeleworkBalanceById(@Argument String id) {
        return teleworkBalanceService.getTeleworkBalanceById(id);
    }

    @MutationMapping
    public Response createTeleworkBalance(@Argument TeleworkBalance teleworkBalance) {
        return teleworkBalanceService.createTeleworkBalance(teleworkBalance);
    }

    @MutationMapping
    public Response updateTeleworkBalance(@Argument TeleworkBalance teleworkBalance , @Argument String id) {
        return teleworkBalanceService.updateTeleworkBalance(teleworkBalance , id);
    }

    @MutationMapping
    public Response deleteTeleworkBalance(@Argument String id) {
        return teleworkBalanceService.deleteTeleworkBalance(id);
    }
}
